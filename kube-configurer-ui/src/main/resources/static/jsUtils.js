console.log('jsUtils script is loaded!')

$(document).ready(function () {
    $('#btn-create-config').click(function (e) {
        e.preventDefault();
        let file = $('#file').val();
        if (file != null && file !== '') {
            console.log('do smth with file...')
            console.log(file);
            return;
        }
        //todo: tags!
        let configuration = JSON.stringify({
            id: $('#id').val(),
            name: $('#name').val(),
            runArgs: $('#runArgs').val(),
            replicas: $('#replicas').val(),
            cpuLimit: $('#cpuLimit').val(),
            cpuRequest: $('#cpuRequest').val(),
            memoryLimit: $('#memoryLimit').val(),
            memoryRequest: $('#memoryRequest').val()
        })
        console.log('Send request to create config: ' + configuration);
        //todo: validation
        sendAjaxRequest("POST", "/api/configuration", configuration).then(function (res) {
                window.location.href = "/configuration/" + res.id;
            }
        )
    });
});

function sendAjaxRequest(type, url, body) {
    return $.ajax({
        type: type,
        contentType: "application/json",
        url: url,
        xhrFields: {
            withCredentials: true
        },
        data: body,
        cache: false,
        timeout: 600000
    })
}