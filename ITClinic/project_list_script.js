
var projects = [{
  title: 'Web-приложение для аграрного предприятия',
  status_bar: 71,
  description: 'Паровозы серии "ОВ", получившие в народе название "овечка", начали выпускать еще в царской России в 1912 году.',
  array_li: ['Web', 'Низкая сложность', 'Срочный', 'Design', '...'],
  array_li_sublist: ['tag1', 'tag2']
}, {
  title: 'Развертка пингвиньих бегов на местности.',
  status_bar: 51,
  description: 'Нормальное число зубов у улитки - от 15 до 25 тысяч. Самый быстрый из пингвинов - субантарктический, или папуанский пингвин.',
  array_li: ['Анализ данных', 'Низкая сложность', 'Тег 2', 'Еще один тег', '...'],
  array_li_sublist: ['tag1', 'tag2', 'tag23']
}, {
  title: 'Шифр Цезаря',
  status_bar: 23,
  description: 'Зашифровать Брута салатом Цезарь.',
  array_li: ['Python', 'Средняя сложность', 'Machine Learning', 'Еще один тег', '...'],
  array_li_sublist: ['tag1', 'tag2', 'tag23']
},{
  title: 'Вёрстка ленты Мёбиуса',
  status_bar: 88,
  description: 'Лента Мёбиуса - пример неориентируемой односторонней поверхности с одним краем в обычном трёхмерном Евклидовом пространстве.',
  array_li: ['Анализ данных', 'Низкая сложность', 'Вёрстка', 'Design', '...'],
  array_li_sublist: ['tag1', 'tag2', 'doka 2']
}, {
  title: 'Расшифровка Энигмы',
  status_bar: 22,
  description: 'Расшифровать машину Энигма и при этом не попасть в её "Black Hole"',
  array_li: ['Анализ данных', 'Высокая сложность', 'Dota 2', 'Enigma', '...'],
  array_li_sublist: ['tag1', 'tag2']
}, {
  title: 'Сдать зачет по ВЕБу',
  status_bar: 0, 
  description: 'Написать сайт, который будет удовлетворять всем требованиям преподавателя. Сайт должен быть serverless, должен содержать авторизацию. Оплата почасовая',
  array_li: ['WEB', 'Высокая сложность', 'JS', 'Serverless'],
  array_li_sublist: []
}];


var button_id = 0;

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
  on_project_link.id = button_id;
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

  button_id++;

  return listItem;
};

var boxList = document.querySelector('.list-of-projects');
for (var i = 0; i < projects.length; i++) {
  var boxItem = createBox(projects[i]);
  boxList.appendChild(boxItem);
}

var count = 0;
var count_projects = 0;
var array_tags = [];
// var hidden_count = 0;
document.querySelectorAll('.project-box').forEach(element => {
  array_tags.push(element);
});

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
      });

      var hidden_count = 0;
      document.querySelectorAll('.project-box').forEach(box => {
        if (box.style.display == 'none') {
          hidden_count++;
        }
      });
      if (hidden_count == projects.length) {
          document.querySelector('.message').style.display = 'block';
        }
      hidden_count = 0;
      
      // if (count_projects == 0) {
      //   document.querySelector('.message').style.display = 'block';
      // }
      
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

// document.querySelectorAll('.type-of-tags button').forEach((button) => {
//   button.addEventListener('click', () => {
//     hidden_count = 0;
//     if (count < 3) {
//       var reset = document.querySelector('.reset');
//       reset.style.display = 'block';
//       //array_tags.push(button);

//       count++;
//       count_projects = 0;
      
//       array_tags.forEach(box => {
//         var tags = box.querySelectorAll('.topmenu-tags li');
//         var flag = false;
//         tags.forEach(tag => {
//           //alert(tag.textContent.toLowerCase() + '    ' + button.textContent.toLowerCase())
//           if (tag.textContent.toLowerCase() == button.textContent.toLowerCase()) {
//             flag = true;
//             // array_tags.push(tag);
//             count_projects++;
//           } 
//         });
//         if (flag == false) {
//           alert(box.querySelector('.title').textContent + ' index =   ' + array_tags.indexOf(box));
//           box.style.display = 'none';
//           var idx = array_tags.indexOf(box);
//           array_tags.splice(idx, 1);
//           hidden_count++;
//         }
//         if (array_tags.length == 0) {
//             document.querySelector('.message').style.display = 'block';
//         }
        
//         });
//       // alert(projects.length + '  ' + hidden_count);
//       var ull = document.querySelector('.selected-tags');
//       var tag = document.createElement('div');
//       tag.textContent = button.textContent;
//       tag.style.backgroundColor = window.getComputedStyle( button , null).getPropertyValue('background-color');
//       tag.classList.add('pressed');
//       ull.appendChild(tag);
//       button.disabled = true;

//     } else {
//       alert('Максимум 3 тега!!!')
//     }
  
//   });
// });

var reset = document.querySelector('.reset');

var Reset = function() {
  document.querySelectorAll('.project-box').forEach(element => {
    array_tags.push(element);
  });
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

    element.parentNode.removeChild(element);
  });
};

reset.onclick = Reset;


var on_project_button = document.querySelectorAll('.on-project-page');
on_project_button.forEach((button) => {
  button.addEventListener('click', () => {
    var i = button.id;
    var project = {
      title: projects[i].title,
      flag: projects[i].flag,
      top_list: projects[i].array_li,
      sub_list: projects[i].array_li_sublist,
      difficulty: projects[i].difficulty
    };

  });
});
