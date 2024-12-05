function addMenuToggle() {
    document
        .querySelectorAll("a.navbar-burger")
        .forEach(button => {
            const menuId = button.dataset.target;

            button.addEventListener("click", (event) => {
                const menu = document.getElementById(menuId);
                const isExpanded = button.ariaExpanded === "true";

                button.ariaExpanded = isExpanded.toString();
                button.classList.toggle("is-active");
                menu.classList.toggle("is-active");

                event.preventDefault();
            })
        });
}

document.addEventListener("DOMContentLoaded", () => {
    addMenuToggle();
});
