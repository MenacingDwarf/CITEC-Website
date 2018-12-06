var projects;
var tags_for_bar = [];

var count = 0;

$(document).ready(function() {
    $.ajax({
    type: "GET",
    url: "https://agile-depths-28542.herokuapp.com/api/public/projects?limit=10&offset=0&metrics=progress,countclosed",
    dataType: "json",
    headers: {
      'Authorization': '8006590e56914f96bb2a0d67a998a234'
    },
    success: function(data) {
        projects = data;
        preloader();
        createTags(data);
        create(data);
    }
  });
});

var createTags = function(projects) {
  var reset = document.querySelector('.reset');
  reset.onclick = Reset;
  for (let i = 0; i < projects.length; i++) {
    for (let j = 0; j < projects[i].tags.length; j++) {
      var element = projects[i].tags[j];
      tags_for_bar.push(element);
    }
  }

  tags_for_bar = new Set(tags_for_bar);
  tags_for_bar = Array.from(tags_for_bar);
  console.log(tags_for_bar);


  var ulist = document.querySelector('.type-of-tags .first-type-of-tags');
  for (let i = 0; i < tags_for_bar.length; i++) {
    var lItem = document.createElement('li');
    var liBtn = document.createElement('button');
    liBtn.textContent = tags_for_bar[i];
    liBtn.onclick = tags_listener;
    lItem.appendChild(liBtn);
    ulist.appendChild(lItem);
  }
};

function preloader() {
  setInterval(() => {
    let p = $('.preloader');
    p.css('opacity', 0);

    setInterval(() => 
      p.remove(),
      parseInt(p.css('--duration')) * 1000
    );

  }, 500);
}

var create = function(data) {
  var boxList = document.querySelector('.list-of-projects');
  for (var i = 0; i < data.length; i++) {
    var boxItem = createBox(data[i]);
    boxList.appendChild(boxItem);
  }
}

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
  var title_box = makeElement('div', 'title-box', project.name);
  var title = makeElement('div', 'title');
  title.appendChild(title_box);
  listItem.appendChild(title);

  var btn = document.createElement('div');
  var on_project_link = makeElement('a', 'on-project-page', 'На страницу проекта');
  on_project_link.id = project.id;
  on_project_link.setAttribute('href', 'about_project.html');
  on_project_link.addEventListener('click', () => {
    var str = 'id=' + on_project_link.id + '; path=/;';
    document.cookie = str;
    
  });
  btn.appendChild(on_project_link);
  listItem.appendChild(btn);

  var foo = document.createElement('div');
  foo.classList.add('foo');
  if (project.metrics.length == 0) {
    foo.textContent = '0' + '%';
  } else {
    var status = Math.round(project.metrics[0].value * 100);
    foo.textContent = status + '%';
  }

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

  if (project.tags.length == 0) {
    var tags_top = document.createElement('li');
    tags_top.textContent = 'Тегов нет :с';
    tags.appendChild(tags_top);
  } else {
    for (var i = 0; i < project.tags.length; i++) {
      var tags_top = document.createElement('li');
      tags_top.textContent = project.tags[i];
      tags.appendChild(tags_top);
    }
  }


  divTags.appendChild(tags);
  listItem.appendChild(divTags);

  return listItem;
};

var tags_listener = function() {
    if (count < 3) {
      var reset = document.querySelector('.reset');
      reset.style.display = 'block';

      count++;
      var count_projects = 0;
      document.querySelectorAll('.project-box').forEach(box => {
        var tags = box.querySelectorAll('.topmenu-tags li');
        var flag = false;
        tags.forEach(tag => {
          if (tag.textContent.toLowerCase() == this.textContent.toLowerCase()) {
            flag = true;
            count_projects++;
          } 
        });
        if (flag == false) {
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
      
      var ull = document.querySelector('.selected-tags');
      var tag = document.createElement('div');
      tag.textContent = this.textContent;
      tag.style.backgroundColor = window.getComputedStyle( this , null).getPropertyValue('background-color');
      tag.classList.add('pressed');
      ull.appendChild(tag);
      this.disabled = true;

    } else {
      alert('Максимум 3 тега!!!')
    }
}


var array_tags = [];

var Reset = function() {
  document.querySelectorAll('.project-box').forEach(element => {
    array_tags.push(element);
  });
  count = 0;
  this.style.display = 'none';
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

// document.querySelector('.search-button').addEventListener('click', () => {
//   var s = document.querySelector('.search-form .search');
//   if (s.value != '') {
//     document.querySelectorAll('.project-box').forEach(box => {
//       var tags = box.querySelectorAll('.topmenu-tags li');
//       var flag = false;
//       tags.forEach(tag => {
//         if (tag.textContent.toLowerCase() == this.textContent.toLowerCase()) {
//           flag = true;
//           count_projects++;
//         } 
//       });
//       if (flag == false) {
//         box.style.display = 'none';
//       }
//     });

//     var hidden_count = 0;
//       document.querySelectorAll('.project-box').forEach(box => {
//         if (box.style.display == 'none') {
//           hidden_count++;
//         }
//       });
//       if (hidden_count == projects.length) {
//           document.querySelector('.message').style.display = 'block';
//         }
//       hidden_count = 0;

//   }
// });

