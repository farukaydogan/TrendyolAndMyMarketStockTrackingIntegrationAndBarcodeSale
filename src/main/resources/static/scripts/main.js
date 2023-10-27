let accumulatedKeys = "";
let lastKeypressTime = 0;


var messageType = [[${messageType}]];
var message = [[${message}]];

window.onload = function () {
    if (messageType && message) {
        alert(message);
    }
};
document.addEventListener('keydown', function (e) {
    const key = e.key; // tuş basımını al
    const currentTime = new Date().getTime(); // şu anki zamanı al

    if (currentTime - lastKeypressTime > 100) {
        // eğer son tuş basımı ile şu anki zaman arasında 100ms'den fazla varsa
        // toplama buffer'ını sıfırla
        accumulatedKeys = "";
    }

    // tuş basımını toplama buffer'ına ekle
    accumulatedKeys += key;

    // zaman damgasını güncelle
    lastKeypressTime = currentTime;

    // eğer toplama buffer'ı 13 karaktere ulaştıysa
    if (accumulatedKeys.length === 13) {
        // burada istediğiniz işlemleri yapabilirsiniz,
        // örneğin bir input alanını doldurabilirsiniz:
        document.getElementById('barcode').value = accumulatedKeys;
        // toplama buffer'ını sıfırla
        accumulatedKeys = "";
    }
});
