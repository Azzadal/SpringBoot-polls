for (let i = 0; i < pollMenuTitle.length; i++){
    pollMenuTitle[i].addEventListener('click', function (e) {
        if (!pollMenuContent[i].classList.contains('show')){
            pollMenuContent[i].classList.add('show');
            pollMenuContent[i].style.height = 'auto';
        } else {
            pollMenuContent[i].style.height = '0px';
            pollMenuContent[i].classList.remove('show');
        }
    })
}