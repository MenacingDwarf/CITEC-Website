
var projects = [{
  title: 'Web-приложение для аграрного предприятия',
  status_bar: 71,
  description: 'Тут очень краткое описание проекта, что, как, куда, зачем. Тут я напишу еще текста, чтобы как будто описание. Паровозы серии "ОВ", получившие в народе название "овечка", начали выпускать еще в царской России в 1912 году.',
  array_li: ['Web', 'Низкая сложность', 'Срочный', 'Design', '...'],
  array_li_sublist: ['tag1', 'tag2']
}, {
  title: 'Развертка пингвиньих бегов на местности.',
  status_bar: 51,
  description: 'Я пока не придумала, как это должно выглядеть, но хочу что-то красивое и новое. А то все эти пмовские сайты - это грустно. Нормальное число зубов у улитки - от 15 до 25 тысяч. Самый быстрый из пингвинов - субантарктический, или папуанский пингвин.',
  array_li: ['Анализ данных', 'Низкая сложность', 'Тег 2', 'Еще один тег', '...'],
  array_li_sublist: ['tag1', 'tag2', 'tag23']
}, {
  title: 'Вёрстка ленты Мёбиуса',
  status_bar: 88,
  description: 'Больше бессмысленных текстов, чтобы можно было оценить, как это смотрится и что из этого получится. Хочу печеньку. Лента Мёбиуса - пример неориентируемой односторонней поверхности с одним краем в обычном трёхмерном Евклидовом пространстве.',
  array_li: ['Анализ данных', 'Высокая сложность', 'Вёрстка', 'Design', '...'],
  array_li_sublist: ['tag1', 'tag2', 'doka 2']
}, {
  title: 'Расшифровка Энигмы',
  status_bar: 22,
  description: 'А тут совершенно другое описание совершенно другого проекта. Необходим объект огромной массы, чтобы создать гравитационное поле, достаточное для того, чтобы согнуть пространство и время пополам, и показанный в фильме вариант эквивалентен сотне миллионов наших солнц.',
  array_li: ['Анализ данных', 'Высокая сложность', 'Dota 2', 'Enigma', '...'],
  array_li_sublist: ['tag1', 'tag2']
}, {
  title: 'Сдать зачет по ВЕБу',
  status_bar: 0, 
  description: 'Написать сайт, который будет удовлетворять всем требованиям преподавателя. Сайт должен быть serverless, должен содержать авторизацию. Оплата почасовая',
  array_li: ['WEB', 'Сложная сложность', 'JS', 'Serverless'],
  array_li_sublist: []
}];

var makeElement = function(tagName, className, text) {
  var element = document.createElement(tagName);
  element.classList.add(className);

  if (text) {
    element.textContent = text;
  }
  return element;
};

var createBox = function(project) {

  var listItem = makeElement('li', 'project-box');
  var title_box = makeElement('div', 'title-box', project.title);
  var title = makeElement('div', 'title');
  title.appendChild(title_box);
  listItem.appendChild(title);

  var btn = document.createElement('div');
  var on_project_link = makeElement('a', 'on-project-page', 'На страницу проекта');
  on_project_link.setAttribute('href', 'about_project.html');
  btn.appendChild(on_project_link);
  listItem.appendChild(btn);

  var foo = document.createElement('div');
  foo.classList.add('foo');
  foo.textContent = project.status_bar + '%';

  var st_bar = document.createElement('div');
  st_bar.classList.add('status-bar');
  st_bar.style.width = foo.textContent;

  foo.appendChild(st_bar);
  listItem.appendChild(foo);

  var desc = makeElement('div', 'short-description', project.description);
  listItem.appendChild(desc);

  var divTags = document.createElement('div');
  divTags.classList.add('tags');

  var tags = document.createElement('ul');
  tags.classList.add('topmenu-tags');


  var subUl = document.createElement('ul');
  subUl.classList.add('submenu-tags');
  
  for (var i = 0; i < project.array_li_sublist.length; i++) {
    var subtag = document.createElement('li');
    subtag.textContent = project.array_li_sublist[i];
    subUl.appendChild(subtag);
  }
  
  
  for (var i = 0; i < project.array_li.length; i++) {
    var tags_top = document.createElement('li');
    tags_top.textContent = project.array_li[i];
    if (i === project.array_li.length - 1) {
      tags_top.classList.add('others');
      var fa_fa = document.createElement('i');
      fa_fa.classList.add('fa');
      tags_top.appendChild(fa_fa);
      tags_top.appendChild(subUl);
    }
    tags.appendChild(tags_top);
  }


  divTags.appendChild(tags);
  listItem.appendChild(divTags);

  return listItem;
};

var boxList = document.querySelector('.list-of-projects');
for (var i = 0; i < projects.length; i++) {
  var boxItem = createBox(projects[i]);
  boxList.appendChild(boxItem);
}

var count = 0;


document.querySelectorAll('.type-of-tags button').forEach((button) => {
  button.addEventListener('click', () => {
    if (count < 3) {
      var reset = document.querySelector('.reset');
      reset.style.display = 'block';

      count++;
      var count_projects = 0;
      document.querySelectorAll('.project-box').forEach(box => {
        // alert('okkk');
        var tags = box.querySelectorAll('.topmenu-tags li');
        var flag = false;
        tags.forEach(tag => {
          if (tag.textContent.toLowerCase() == button.textContent.toLowerCase()) {
            flag = true;
            count_projects++;
            // break;
          } 
        });
        if (flag == false) {
          // alert('false');
          box.style.display = 'none';
        }
      if (count_projects == 0) {
        document.querySelector('.message').style.display = 'block';
      }

      });
      
      var ull = document.querySelector('.selected-tags');
      var tag = document.createElement('div');
      tag.textContent = button.textContent;
      tag.style.backgroundColor = window.getComputedStyle( button , null).getPropertyValue('background-color');
      tag.classList.add('pressed');
      ull.appendChild(tag);
      button.disabled = true;

    } else {
      alert('Максимум 3 тега!!!')
    }
  });
});

var reset = document.querySelector('.reset');

var Reset = function() {
  count = 0;
  reset.style.display = 'none';
  document.querySelectorAll('.type-of-tags button').forEach(elem => {
    elem.disabled = false;
  });

  document.querySelectorAll('.project-box').forEach(elem => {
    elem.style.display = 'block';
  });
  
  var parentElem = document.querySelector('.selected-tags');

  document.querySelector('.message').style.display = 'none';

  document.querySelectorAll('.selected-tags div').forEach(element => {
    // element.style.display = 'none';
    element.parentNode.removeChild(element);
  });
}

reset.onclick = Reset;