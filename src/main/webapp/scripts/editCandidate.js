$(document).ready(loadCities);

/**
 * Метод для получения списка городов с сервера
 * и вывода на странице редактирования кандидата.
 */
function loadCities() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/dreamjob/ajax/getCities.do',
        dataType: 'json',
        beforeSend: function(request) {
            request.setRequestHeader("Authority", $('#ajaxToken').text());
        }
    }).done(function (data) {
        let response = JSON.parse(JSON.stringify(data));
        for (const [key, value] of Object.entries(response)) {
            let option = key === $('#currentCity').text() ? 'option selected' : 'option';
            let nextCity = '<' + option + ' value="' + key + '">' + value + '</>';
            $('#selectedCity').append(nextCity);
        }
    }).fail(function (error) {
        $('#selectedCity').append('<option value="0">UNKNOWN</option>');
    })
}

/**
 * Валидация данных введенных пользователем.
 * Должно быть заполнено имя и выбран город.
 */
$('#candidateInput').submit(function (event) {
    if ($('#nameInput > input').val() !== "" && document.getElementById('selectedCity').value != 0) {
        return;
    }
    if ($('#nameInput > input').val() === "") {
        if (!$.contains(document.querySelector('#nameInput'), document.querySelector('#nameInput > .alert'))) {
            $('#nameInput').append('<div class="alert alert-danger" role="alert">' +
                'Нужно заполнить имя!' +
                '</div>');
        }
        $('#nameInput > .alert').show().fadeOut(1000);
        event.preventDefault();
    } else if (document.getElementById('selectedCity').value == 0) {
        if (!$.contains(document.querySelector('#cityInput'), document.querySelector('#cityInput > .alert'))) {
            $('#cityInput').append('<div class="alert alert-danger" role="alert">' +
                'Нужно заполнить город!' +
                '</div>');
        }
        $('#cityInput > .alert').show().fadeOut(1000);
        event.preventDefault();
    }
});