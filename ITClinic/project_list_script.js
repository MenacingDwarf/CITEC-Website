
var projects;



$(document).ready(function() {
    $.ajax({
    type: "GET",
    url: "https://agile-depths-28542.herokuapp.com/api/projects?limit=5&offset=0&metrics=random", //, another", 
    // url: "https://agile-depths-28542.herokuapp.com/api/projects/info",
    dataType: "json",
    headers: {
      'Authorization': '8006590e56914f96bb2a0d67a998a234'
    },
  //   error: function(request, status, error) {
  //     alert(request.status); // вот он код ответа
  //  },
    // beforeSend: function() {
    //   alert('loading');
    // },
    success: function(data) {
        // data = JSON.parse(data);
        // alert('Title ' + data[0].name + ' Client -  '  + data[0].client + ' repo ' + data[0].githubRepo);
        projects = data;
        preloader();
        create(data);
        // alert(data.active);
        // alert(data);
    }
  });
});

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


// var button_id = 0;

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

// function getCookie(name) {
//   var matches = document.cookie.match(new RegExp(
//     "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
//   ));
//   return matches ? decodeURIComponent(matches[1]) : undefined;
// }


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
    //alert(on_project_link.id);
    var str = 'id=' + on_project_link.id + '; path=/;';
    // alert('str = ' + str);
    document.cookie = str;
    
  });
  btn.appendChild(on_project_link);
  listItem.appendChild(btn);

  var foo = document.createElement('div');
  foo.classList.add('foo');
  if (project.metrics.length == 0) {
    foo.textContent = '0' + '%';
  } else {
    var status = Math.round(project.metrics[project.metrics.length - 1].value * 100);
    foo.textContent = status + '%';
  }
  // foo.textContent = project.status_bar + '%';

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


  // var subUl = document.createElement('ul');
  // subUl.classList.add('submenu-tags');
  
  // for (var i = 0; i < project.array_li_sublist.length; i++) {
  //   var subtag = document.createElement('li');
  //   subtag.textContent = project.array_li_sublist[i];
  //   subUl.appendChild(subtag);
  // }
  
  if (project.tags.length == 0) {
    var tags_top = document.createElement('li');
    tags_top.textContent = 'Тегов нет :с';
    tags.appendChild(tags_top);
  } else {
    for (var i = 0; i < project.tags.length; i++) {
      var tags_top = document.createElement('li');
      tags_top.textContent = project.tags[i];
      // if (i === project.array_li.length - 1) {
      //   tags_top.classList.add('others');
      //   var fa_fa = document.createElement('i');
      //   fa_fa.classList.add('fa');
      //   tags_top.appendChild(fa_fa);
      //   tags_top.appendChild(subUl);
      // }
      tags.appendChild(tags_top);
    }
  }


  divTags.appendChild(tags);
  listItem.appendChild(divTags);

  // var button_id = project.id;

  return listItem;
};

// var boxList = document.querySelector('.list-of-projects');
// for (var i = 0; i < projects.length; i++) {
//   var boxItem = createBox(projects[i]);
//   boxList.appendChild(boxItem);
// }

var count = 0;
var count_projects = 0;
var array_tags = [];
var hidden_count = 0;
// document.querySelectorAll('.project-box').forEach(element => {
//   array_tags.push(element);
// });

document.querySelectorAll('.type-of-tags button').forEach((button) => {
  button.addEventListener('click', () => {
    // alert('ok');
    if (count < 3) {
      var reset = document.querySelector('.reset');
      reset.style.display = 'block';

      count++;
      var count_projects = 0;
      document.querySelectorAll('.project-box').forEach(box => {
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

var reset = document.querySelector('.reset');

// var Reset = function() {
//   count = 0;
//   reset.style.display = 'none';

//   document.querySelectorAll('.project-box').forEach(elem => {
//     elem.style.display = 'block';
//   });

//   document.querySelector('.message').style.display = 'none';
// };

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

// var on_project_button = document.querySelector('.on-project-page');
// on_project_button.addEventListener('click', () => {
//   alert('ok');
// });

// on_project_button[0].addEventListener('click', () => {
//   alert('hallo');
// });

// on_project_button.forEach((button) => {
//   button.addEventListener('click', () => {
//     //var i = button.id;
//     alert('okay');
//     // about_project_script.che(i);

//   });
// });



// var check = function(id) {
//   var d = document.querySelector('.status-project');
//   alert(d);
// };