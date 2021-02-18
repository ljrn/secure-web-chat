const send = document.querySelector('#send');
const form = document.querySelector('form');
const textArea = document.querySelector('#message')

send.onclick= function(e){
    e.preventDefault();
    if(textArea.value)form.submit();
}

textArea.onkeypress = function(e){
    if(e.which === 13 && !e.shiftKey) {
        e.preventDefault();
        if(textArea.value)form.submit();
    }
}

var chat = document.querySelector('.chat');
chat.scrollTop = chat.scrollHeight;