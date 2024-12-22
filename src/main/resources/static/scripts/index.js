function requestAnimationAfter(callback, timeoutMilliseconds) {
    let initialTimestamp;

    requestAnimationFrame(function checkAnimationLoop(timestamp) {
        initialTimestamp ??= timestamp;
        const elapsed = timestamp - initialTimestamp;

        if (elapsed >= timeoutMilliseconds) {
            return callback();
        }

        requestAnimationFrame(checkAnimationLoop);
    });
}

const CLICK_ACTION_MAP = Object.freeze({
    "toggle-dropdown": {
        accepts: [],
        trigger: ({ $event }) => {
            const toggle = $event.currentTarget;
            const shouldExpand = toggle.ariaExpanded === "false";

            toggle.ariaExpanded = shouldExpand;

            if (shouldExpand) {
                function closeDropdown() {
                    toggle.ariaExpanded = false;
                }

                setTimeout(() => {
                    window.addEventListener("click", closeDropdown, { once: true });
                }, 0);
            }
        }
    },
    "open-dialog": {
        accepts: ["targetSelector"],
        trigger: ({ targetSelector }) => {
            const targetElement = document.querySelector(targetSelector);

            if (targetElement === null) {
                return;
            }

            const closeDialogButtons = targetElement.querySelectorAll("[data-action=\"close-dialog\"]");

            function closeDialog({ target, currentTarget }) {
                const isOutsideClick = currentTarget === targetElement && target === targetElement;
                const isButtonClick = currentTarget !== targetElement;

                if (!isOutsideClick && !isButtonClick) {
                    return;
                }

                targetElement.classList.remove("is-open");

                targetElement.removeEventListener("click", closeDialog);
                closeDialogButtons.forEach((closeDialogButton) => closeDialogButton.removeEventListener("click", closeDialog));
            }

            targetElement.addEventListener("click", closeDialog);
            closeDialogButtons.forEach((closeDialogButton) => {
                closeDialogButton.addEventListener("click", closeDialog);
            });

            targetElement.classList.add("is-open");
       }
    },
    "open-drawer": {
        accepts: ["targetSelector"],
        trigger: ({ targetSelector }) => {
            const targetElement = document.querySelector(targetSelector);

            if (targetElement === null) {
                return;
            }

            document.body.overflow = "hidden";

            targetElement.classList.add("is-visible");

            requestAnimationAfter(() => {
                targetElement.classList.add("is-open");
            }, 0);
        }
    },
    "close-drawer": {
        accepts: [],
        trigger: ({ $event }) => {
            const targetElement = $event.currentTarget;

            const isOutsideClick = targetElement === $event.target;

            if (!isOutsideClick) {
                return;
            }

            targetElement.classList.remove("is-open");

            requestAnimationAfter(() => {
                targetElement.classList.remove("is-visible");

                document.body.overflow = null;
            }, 200);
        }
    },
});

function extractActionValues(element, keys) {
    const actionValues = {};

    for (const key of keys) {
        const actionValue = element.dataset[key];

        if (typeof actionValue !== "string") {
            return null;
        }

        actionValues[key] = actionValue;
    }

    return actionValues;
}

function registerClickHandlers() {
    Object
        .entries(CLICK_ACTION_MAP)
        .forEach(([key, action]) => {
            document
                .querySelectorAll(`[data-click-action="${key}"]`)
                .forEach((element) => {
                    const actionValues = extractActionValues(element, action.accepts);

                    if (actionValues === null) {
                        return;
                    }

                    function handleEvent($event) {
                        actionValues.$event = $event;

                        action.trigger(actionValues);
                    }

                    element.addEventListener("click", handleEvent);
                });
        });
}

function main() {
    registerClickHandlers();
}

document.addEventListener("DOMContentLoaded", main, { once: true });