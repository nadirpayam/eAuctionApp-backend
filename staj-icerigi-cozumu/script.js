const socket = new WebSocket("wss://cekirdektenyetisenler.kartaca.com/ws");

let isRegistered = false;

socket.addEventListener("open", function(event) {
  console.log("WebSocket bağlantısı açıldı.");
});

socket.addEventListener("message", function(event) {
  const message = JSON.parse(event.data);
  console.log("Mesaj alındı:");
  console.log("Gönderen: ", message.sender);
  console.log("Zaman damgası: ", message.timestamp);
  console.log("Mesaj: ", message.message);

  if (!isRegistered) {
    const lastChars = message.message.slice(-64);
    console.log("Son 65 karakter:", lastChars);
    const nee = harfDegistir(lastChars);
    const hee = nee.toString();
    const registerMessage = {
      type: "REGISTER",
      name: "nadir",
      surname: "payam",
      email: "nadirpayam@hotmail.com",
      registrationKey: hee
    };
    console.log("key:" + hee)
    
    socket.send(JSON.stringify(registerMessage));
    isRegistered = true;
  }
});


socket.addEventListener("close", function(event) {
  console.log("WebSocket bağlantısı kapatıldı.");
});


function harfDegistir(text) {
  var harfMap = {
    'a': 'z',
    'b': 'y',
    'c': 'x',
    'd': 'w',
    'e': 'v',
    'f': 'u',
    'g': 't',
    'h': 's',
    'i': 'r',
    'j': 'q',
    'k': 'p',
    'l': 'o',
    'm': 'n',
    'n': 'm',
    'o': 'l',
    'p': 'k',
    'q': 'j',
    'r': 'i',
    's': 'h',
    't': 'g',
    'u': 'f',
    'v': 'e',
    'w': 'd',
    'x': 'c',
    'y': 'b',
    'z': 'a'
  };

  var yeniMetin = "";

  for (var i = 0; i < text.length; i++) {
    var karakter = text[i];
    var yeniKarakter = harfMap[karakter] || karakter;
    yeniMetin += yeniKarakter;
  }

  return yeniMetin;
}

