document.addEventListener("DOMContentLoaded", function() {
    fetch("/portal-facil/logo-path")
        .then(response => response.text())
        .then(base64Image => {
            const logoElement = document.getElementById("logoApp");
            if (logoElement) {
                if (base64Image && base64Image.trim() !== "") {
                    logoElement.style.backgroundImage = `url('${base64Image}')`;
                    logoElement.style.visibility = "visible"; // Mostrar cuando hay imagen base64
                } else {
                    // No hay base64, mostramos la imagen CSS normal
                    logoElement.style.visibility = "visible";
                    console.warn("Logo especial no encontrado, usando el del base.css");
                }
            }
        })
        .catch(err => {
            console.error("Error consultando el logo:", err);
            const logoElement = document.getElementById("logoApp");
            if (logoElement) {
                logoElement.style.visibility = "visible"; // Mostrar logo predeterminado
            }
        });
});
