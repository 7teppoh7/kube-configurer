console.log('jsUtils script is loaded!')

const selectedService = "selectedService"
const selectedConfig = "selectedConfig"

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

    $('button[name=btn-show-message-modal]').click(function (e) {
        let message = this.value
        $('#showMessagePlace').text(message)
    });

    $('#ns-select').change(function (e) {
        let selectedValue = this.options[this.selectedIndex].value;
        let url = "/api/services"
        if (selectedValue !== "_all") {
            url += "?namespace=" + selectedValue
        }
        sendAjaxRequest("GET", url, null).then(function (res) {
                redrawServices(res)
            }
        )
    });

    $('input[name=selectedService]').click(function (e) {
        let selected = $('input[name=selectedService]:checked')
        if (selected.length > 0) {
            let searchParams = new URLSearchParams(window.location.search)
            searchParams.set(selectedService, selected[0].value);
            let newRelativePathQuery = window.location.pathname + '?' + searchParams.toString();
            history.pushState(null, '', newRelativePathQuery);
        }
    })

    $('input[name=selectedConfig]').click(function (e) {
        let selected = $('input[name=selectedConfig]:checked')
        if (selected.length > 0) {
            let searchParams = new URLSearchParams(window.location.search)
            searchParams.set(selectedConfig, selected[0].value);
            let newRelativePathQuery = window.location.pathname + '?' + searchParams.toString();
            history.pushState(null, '', newRelativePathQuery);
        }
    })

    $('#btn-configure').click(function (e) {
        e.preventDefault();
        let searchParams = new URLSearchParams(window.location.search)
        let configId = searchParams.get(selectedConfig)
        let serviceId = searchParams.get(selectedService)
        let configureBody = JSON.stringify({
            serviceId: serviceId,
            configId: configId,
        })
        sendAjaxRequest("POST", "/api/configure", configureBody).then(function (res) {
            console.log(res)
        })
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

function redrawServices(services) {
    let isConfigure = $('#input-hidden-is-configure').val()
    $('#serviceTable').empty();
    services.forEach(service => {
        addService(service, isConfigure);
    });
}

function addService(service, isConfigure) {
    let configItem
    if (service['configuration'] != null) {
        configItem = '<td><a href="/configuration/' + service['configuration']['id'] + '">' + service['configuration']['name'] + '</a></td>'
    } else {
        configItem = '<td><p>Конфигурация отсутствует</p></td>'
    }
    let dateItem
    if (service['lastAction'] !== null) {
        let date = new Date(service['lastAction']['date'])
        let day = date.getDate();
        let month = date.getMonth() + 1;
        let hour = date.getHours();
        let minute = date.getMinutes();
        if (day < 10) {
            day = '0' + day
        }
        if (month < 10) {
            month = '0' + month
        }
        if (hour < 10) {
            hour = '0' + hour
        }
        if (minute < 10) {
            minute = '0' + minute
        }
        let strDate = day + '.' + month + '.' + date.getFullYear() + ', ' + hour + ':' + minute
        dateItem = '<td><span>' + strDate + '</span></td>'
    } else {
        dateItem = '<td><span>Нет данных</span></td>'
    }
    let statusItem
    if (service['lastAction'] !== null) {
        if (service['lastAction']['status'] === 'IN_PROGRESS') {
            statusItem = '<td><span class="badge badge-pill badge-warning">Применяется</span></td>'

        } else if (service['lastAction']['status'] === 'FAILED') {
            statusItem = '<td><span class="badge badge-pill badge-danger">Ошибка</span></td>'

        } else if (service['lastAction']['status'] === 'SUCCESS') {
            statusItem = '<td><span class="badge badge-pill badge-success">Успешно</span></td>'
        }
    } else {
        statusItem = '<td><span class="badge badge-pill badge-secondary">Нет данных</span></td>'
    }

    let actionItem
    if (isConfigure !== undefined && isConfigure === 'true') {
        actionItem = '<td><input class="form-check-input" type="radio" name="selectedService" id="selectedService' + service['id'] + '" value="' + service['id'] + '">' +
            '<label class="form-check-label" for="selectedService' + service['id'] + '">Выбрать</label></td>'
    } else {
        actionItem = '<td><button type="button" class="btn btn-sm btn-success" name="btn-service-configure" value="' + service['id'] + '"> Применить</button></td>'
    }
    let newService = $('<tr>' +
        '<td> <a href="/service/' + service['id'] + '">' + service['deploymentName'] + '</a></td>' +
        '<td><p class="fw-normal mb-1">' + service['namespace'] + '</p></td>' +
        configItem +
        dateItem +
        statusItem +
        actionItem +
        '</tr>');
    $('#serviceTable').append(newService);

    //todo: add onclick for service table
    $('input[name=selectedService]').click(function (e) {
        let selected = $('input[name=selectedService]:checked')
        if (selected.length > 0) {
            let searchParams = new URLSearchParams(window.location.search)
            searchParams.set(selectedService, selected[0].value);
            let newRelativePathQuery = window.location.pathname + '?' + searchParams.toString();
            history.pushState(null, '', newRelativePathQuery);
        }
    })

    $('input[name=selectedConfig]').click(function (e) {
        let selected = $('input[name=selectedConfig]:checked')
        if (selected.length > 0) {
            let searchParams = new URLSearchParams(window.location.search)
            searchParams.set(selectedConfig, selected[0].value);
            let newRelativePathQuery = window.location.pathname + '?' + searchParams.toString();
            history.pushState(null, '', newRelativePathQuery);
        }
    })
}
