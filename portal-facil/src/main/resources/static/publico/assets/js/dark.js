document.addEventListener("DOMContentLoaded", () => {
    let switcher = document.querySelector(".switcher__btn");
    let light = document.querySelector(".switcher__icon-light");
    let dark = document.querySelector(".switcher__icon-dark");
    switcher.addEventListener("click", () => {
        //let head = document.querySelector("head");
        let head = document.head;
        let link = document.createElement("link");
        link.rel = "stylesheet";
        link.type = "text/css";
        link.href = "./publico/assets/css/dark.css";
        link.id = "theme-dark";
        let theme_dark = document.querySelector("#theme-dark");
        if ( theme_dark ) {
            head.removeChild(theme_dark);
            light.style.display = "block";
            dark.style.display = "none";
        } else {
            head.appendChild(link);
            dark.style.display = "block";
            light.style.display = "none";
        }
        
    });
});