
let modal
const pollMenuTitle = document.getElementsByClassName('poll_menu_title')
const pollMenu = document.getElementsByClassName('poll_menu')
const pollMenuDelete = document.getElementsByClassName('poll_menu_delete')
const pollMenuContent = document.getElementsByClassName('poll_menu_content')
const createPoll = document.getElementById('createPoll')
const createQuestion = document.getElementsByClassName('createQuestion')
const editPoll = document.getElementsByClassName('editPoll')
const getPolls = document.getElementById('getPolls')
const pollList = document.getElementsByClassName('poll__list')
const poll = document.getElementsByClassName('poll')
const filter = document.getElementById('filter')
const sortSelect = document.getElementById('sortSelect')
let questTableBody

window.onload = () => {
    //создание опроса
    createPoll.addEventListener('click', () => {
        modal = base.modal({
            title: 'Создание опроса',
            content: `
                <label for="poll_name">Тема опроса</label><input id="poll_name" type="text"/>
                <label for="date_start">Дата начала</label><input id="date_start" type="date">
                <label for="date_end">Дата окончания</label><input id="date_end" type="date">
            `,
            execute(){
                const json = JSON.stringify({
                    name: document.getElementById('poll_name').value,
                    dateStart: document.getElementById('date_start').value,
                    dateEnd: document.getElementById('date_end').value,
                    active: true
                })
                const req = new XMLHttpRequest();
                req.responseType = "json";
                let response
                req.open('POST', window.location + 'api/add_poll');
                req.setRequestHeader("Content-type", "application/json");


                req.onreadystatechange = function() {
                    if (this.readyState === 4) {
                        response = this.response
                        console.log('json', response)
                        let pollMenu = document.createElement('div')
                        let pollMenuTitle = document.createElement('div')
                        let pollMenuContent = document.createElement('div')
                        pollMenu.appendChild(pollMenuTitle)

                        pollMenuTitle.innerHTML = document.getElementById('poll_name').value
                        pollMenuTitle.classList.add("poll_menu_title")

                        pollMenu.appendChild(pollMenuContent)
                        pollMenuContent.innerHTML +=
                            `
                                <button class="createQuestion">Добавить вопрос</button>
                            `
                        pollMenuContent.classList.add("poll_menu_content")

                        pollMenuTitle.addEventListener('click', function () {
                            if (!pollMenuContent.classList.contains('show')){
                                pollMenuContent.classList.add('show');
                                pollMenuContent.style.height = 'auto';
                            } else {
                                console.log('else')
                                pollMenuContent.style.height = '0px';
                                pollMenuContent.classList.remove('show');
                            }
                        })
                        pollList[0].appendChild(pollMenu)
                        pollMenu.classList.add("poll_menu")
                        pollBtnEvent()
                        addQuestionBtnEvent(response)
                    }
                };
                req.send(json);
            }
        })
        setTimeout(modal.open, 1)
    })
    getPolls.addEventListener('click', e => {getPollsFunc()})

}

function getPollsFunc() {
    let n = filter.selectedIndex
    const date = document.getElementById('dateFilter').value
    const name = document.getElementById('nameFilter').value
    let filterOption
    const options = filter.options
    if (n === 1) filterOption = `onDateStart/${date}`
    else if (n === 2) filterOption = `onDateEnd/${date}`
    else if (n === 3) filterOption = `onSubString/${name}`
    else filterOption = options[n].value

    let n1 = sortSelect.selectedIndex

    const xhr = new XMLHttpRequest()
    xhr.responseType = "json";
    xhr.open('GET', window.location + 'get_polls/' + filterOption);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            let json = xhr.response
            if (n1 === 1){
                json.sort((a, b) => {
                    if (a.name > b.name) {
                        return 1;
                    }
                    if (a.name < b.name) {
                        return -1;
                    }
                    return 0;
                })
            }

            pollList[0].innerHTML = ''
            for (let i = 0; i < json.length; i++){
                pollList[0].innerHTML +=
                    `
                            <div class="poll_menu">
                            <span class="poll_menu_delete" data-del="true">&times;</span>
                                <div class="poll_menu_title">
                                    ${json[i].name}
                                </div>
                                <div class="poll_menu_content">
                                    <div class="poll_menu_content_buttons">
                                        <button class="createQuestion">Добавить вопрос</button>
                                        <button class="editPoll">Редактировать опрос</button>
                                    </div>
                                    <div class="quest_table">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Вопросы</th>
                                            </tr>
                                        </thead>
                                        <tbody  class="quest_table_body"></tbody>
                                    </table>
                                </div>
                                </div>
                            </div>
                        `
            }
            getQuestions(json)
            questTableBody = document.getElementsByClassName('quest_table_body');
            function getQuestions(json){
                let response
                for (let i = 0; i < json.length; i++) {
                    const xhr = new XMLHttpRequest()
                    xhr.responseType = "json";
                    xhr.open('GET', window.location + 'get_questions/' + json[i].name);
                    xhr.onreadystatechange = () => {
                        if (xhr.readyState === 4) {
                            response = xhr.response
                            for (let j = 0; j < response.length; j++) {
                                questTableBody[i].innerHTML += '<tr><td>' + response[j].content + '</td></tr>'
                            }
                        }
                    }
                    xhr.send();
                }
            }
            deletePoll(json)
            pollBtnEvent()
            addQuestionBtnEvent(json)
        }
    };
    xhr.send();

    setTimeout(() => {
        const elem = document.createElement('script')
        elem.type = 'text/javascript'
        elem.src = 'js/pollMenu.js'
        document.body.appendChild(elem)
    }, 1000)
}

function deletePoll(json){
    for (let i = 0; i < json.length; i++) {
        pollMenuDelete[i].addEventListener('click', event => {
            const xhr = new XMLHttpRequest()
            xhr.open('DELETE', window.location + 'delete_poll/' + json[i].name);
            xhr.send()
            pollMenu[i].remove()
            setTimeout(getPollsFunc, 1000)
        })
    }
}

function pollBtnEvent() {
    for (let i = 0; i < poll.length; i++){
        poll[i].addEventListener('click', function (){
            modal = base.modal({
                title: this.innerText
            }).open()
        })
    }
}

function addQuestionBtnEvent(theme) {
    //создание вопроса
    let name, start

    if (theme.name !== undefined) start = createQuestion.length - 1
    else start = 0
    for (let i = start; i < createQuestion.length; i++) {
        createQuestion[i].addEventListener('click', () => {
            if (theme[i] !== undefined) name = theme[i].name
            else name = theme.name

            modal = base.modal({
                title: 'Создание вопроса',
                content: `
                <div>
                    <p style="margin: 0">Тема опроса</p><span id="poll_name">${name}</span>
                </div>
                <div>
                    <p>Текст вопроса</p>
                    <textarea id="q_content"></textarea>
                </div>    
            `,
                execute() {
                    const json = JSON.stringify({
                        content: document.getElementById('q_content').value,
                        pollName: name,
                        displayOrder: 'test'
                    })
                    const req = new XMLHttpRequest();
                    req.open('POST', window.location + 'api/add_question');
                    req.setRequestHeader("Content-type", "application/json");
                    req.send(json);
                    getPollsFunc()
                }
            })
            setTimeout(modal.open, 1)
        })

        editPoll[i].addEventListener('click', () => {
            if (theme[i] !== undefined) name = theme[i].name
            else name = theme.name

            modal = base.modal({
                title: 'Редактирование опроса',
                content: `
                <label for="new_poll_name">Тема опроса</label><input id="new_poll_name" type="text"/>
                <label for="new_date_start">Дата начала</label><input id="new_date_start" type="date">
                <label for="new_date_end">Дата окончания</label><input id="new_date_end" type="date">
            `,
                execute() {
                    const json = JSON.stringify({
                        name: document.getElementById('new_poll_name').value,
                        dateStart: document.getElementById('new_date_start').value,
                        dateEnd: document.getElementById('new_date_end').value,
                        active: true
                    })
                    const req = new XMLHttpRequest();
                    req.open('PUT', window.location + `api/edit_poll/${name}`);
                    req.setRequestHeader("Content-type", "application/json");
                    req.send(json);
                    setTimeout(getPollsFunc, 1000)
                }
            })
            setTimeout(modal.open, 1)
        })
    }
}