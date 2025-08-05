let inactivityTime = 10 * 60 * 1000; // 10 minutos
let countdownTime = 30;
let countdownInterval;
let inactivityTimer;
var contextRoot = /*[[@{/}]]*/ '';

function startInactivityTimer() {
  clearTimeout(inactivityTimer);
  inactivityTimer = setTimeout(showSessionModal, inactivityTime);
}

function showSessionModal() {
	
  const modal = document.getElementById("sessionModal");
  const message = document.getElementById("modalMessage");
  const countdown = document.getElementById("countdown");
  const continueBtn = document.getElementById("continueBtn");

  countdownTime = 30;
  countdown.textContent = countdownTime;
  modal.style.display = "flex";

  countdownInterval = setInterval(() => {
    countdownTime--;
    countdown.textContent = countdownTime;
    if (countdownTime <= 0) {
      clearInterval(countdownInterval);
      endSession();
    }
  }, 1000);

  continueBtn.onclick = () => {
    clearInterval(countdownInterval);
    modal.style.display = "none";
    fetch("/portal-facil/authenticate/keep-alive"); // Llama al backend para renovar la sesión
    startInactivityTimer(); // Reinicia conteo
  };
}

function endSession() {
  const modal = document.getElementById("sessionModal");
  document.getElementById("modalMessage").textContent = "Sesión terminada. Vuelva a ingresar.";
  document.getElementById("countdown").style.display = "none";
  document.getElementById("continueBtn").style.display = "none";

  /*setTimeout(() => {
    window.location.href = contextRoot+"logout"; // Redirige al logout
  }, 3000);*/
  
  setTimeout(() => {
    window.location.href = contextRoot+"/portal-facil/logout"; // Redirige al logout
  }, 3000);
}

["click", "mousemove", "keydown"].forEach(event =>
  document.addEventListener(event, startInactivityTimer)
);

startInactivityTimer();