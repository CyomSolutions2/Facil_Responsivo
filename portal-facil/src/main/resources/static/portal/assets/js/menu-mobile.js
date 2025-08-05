document.addEventListener("DOMContentLoaded", (event) => {
   //alert("Sccript cargado");
    let mobile_btn = document.querySelector(".navbar__mobile-btn");
    let mobile_menu = document.querySelector(".menu-mobile");

    const showHiddenMenu = () => {

        let show = document.querySelector(".menu-mobile--show");

        if (show) {
            mobile_menu.classList.remove("menu-mobile--show");
        } else {
            mobile_menu.classList.add("menu-mobile--show");
        }

    };

    // Al dar click al boton del menú mostrar el menú de navegación responsive
    mobile_btn.addEventListener("click", showHiddenMenu );

    // Al redimensionar la pantalla ocultar el menú si es necesario
    window.addEventListener("resize", () => {
        let window_width = parseInt(document.body.clientWidth);

        if ( window_width >= 1000 ) {
           mobile_menu.classList.remove("menu-mobile--show");
        }

    });

    // Poder cerrar el menú con el botón X
    let btn_close = document.querySelector(".menu-mobile__close");
    btn_close.addEventListener("click", showHiddenMenu );

    // Desplegar submenus
    let menu_item = document.querySelectorAll(".menu-mobile__item");

    menu_item.forEach(item => {
        
        item.addEventListener("click", (event) => {
            let submenu = item.lastElementChild;
            if (submenu.className === "menu-mobile__submenu-mobile") {
                
                if ( submenu.style.display === "block" ) {
                  submenu.style.display = "none"; 
                } else {
                  submenu.style.display = "block"; 
                }

                
            }
        });

    });

});