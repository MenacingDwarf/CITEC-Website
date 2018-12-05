// document.cookie = 'name=web;'
// import 'project_list_script';
// alert(abcd);


function getCookie(name) {
  var matches = document.cookie.match(new RegExp(
    "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

var id_project = getCookie('id');
var project;

var find = function(projects) {
  var res;
  for (let i = 0; i < projects.length; i++) {
    if (projects[i].id == id_project) {
      res = projects[i];
      break;
    }
  }
  return res;
}

$(document).ready(function() {
  $.ajax({
    type: "GET",
    url: "https://agile-depths-28542.herokuapp.com/api/projects?limit=5&offset=0&metrics=random", //, another", 
    // url: "https://agile-depths-28542.herokuapp.com/api/projects/info",
    dataType: "json",
    headers: {
      'Authorization': '8006590e56914f96bb2a0d67a998a234'
    },
    success: function(data) {
        project = find(data);
        run(project);
    }
  });
});


// project = {
//   title: 'Название',
//   customer_link: 'ООО "Пупсень и Вупсень"',
//   level: 'Низкая',
//   about_project: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ipsum quia explicabo quidem nesciunt non ipsam sit asperiores laudantium mollitia officia. Assumenda temporibus ab ducimus, nostrum officiis sequi. Magni ex, reiciendis.',
//   graph: 'images/q04319KqOfs.jpg',
//   ghLink: 'хттп.гитхуб.ком',
//   flag: 1,
//   jobs: ['job1', 'job2', 'job3', 'job4', 'job5'],
//   tags: ['Web', 'Низкая сложность', 'Срочный', 'Design', 'JS', 'Serverless']
// };

var makeElement = function(tagName, className, text) {
  var element = document.createElement(tagName);
  element.classList.add(className);

  if (text) 
    element.textContent = text;

  return element;
};


var run = function(project) {
  var desc = document.querySelector('.project-description');

  var title = makeElement('div', 'title-of-project', project.name);
  var graphic = document.querySelector('.chart1');
  desc.insertBefore(title, graphic);
  
  var customer_div = document.createElement('div');
  var customer = makeElement('a', 'customer-link');
  var cust = document.createElement('em');
  cust.textContent = 'Заказчик: ',
  customer.appendChild(cust);
  customer.textContent += project.client;
  customer.setAttribute('href', '');
  customer_div.appendChild(customer);
  desc.insertBefore(customer_div, graphic);

  var h2 = document.createElement('h2');
  h2.textContent = 'О проекте:';
  desc.insertBefore(h2, graphic);

  var project_desc = makeElement('p', 'description', project.description);
  desc.insertBefore(project_desc, graphic);


  var graph = makeElement('div', 'graphic');
  graph.textContent += 'Зелёным цветом отмечены ожидаемые результаты, ';
  graph.textContent += 'красным - результаты реальной работы.';

  var gh = makeElement('div', 'github', 'Github: ');
  var a_gh = document.createElement('a');
  a_gh.href = 'https://github.com';
  a_gh.textContent = project.githubRepo;
  gh.appendChild(a_gh);
  desc.appendChild(gh);

  var content;
  var class_status;

  var jobs = document.querySelector('.list-of-jobs');

  if (project.status == 3) { 
    jobs.style.display = 'none';
    content = 'Завершён'; 
    class_status = 'finished';
  } else if (project.status == 1 || project.status == 2) {
    content = 'В процессе'; 
    class_status = 'in-proccess';
  } else { 
    content = 'Не начат'; 
    class_status = 'in-future';
  }
  

  document.getElementById('status').innerHTML = content;
  document.getElementById('status').classList.add(class_status);

  var color = "hard";

  document.getElementById('level').innerHTML = 'Я не нашел такого в описании проекта(((((';
  document.getElementById('level').classList.add(color);

  var list = document.querySelector('.list');
  var listItem = document.createElement('li');
  listItem.textContent = 'Откуда брать список вакансий?';
  list.appendChild(listItem);
  
  var tags = document.querySelector('.tags');
  if (project.tags.length == 0) {
    var litem = document.createElement('li');
    litem.textContent = 'Тегов нет!!!';
    tags.appendChild(litem);
  } else {
      for (var i = 0; i < project.tags.length; i++) {
      var litem = document.createElement('li');
      litem.textContent = project.tags[i];
      tags.appendChild(litem);
    }
  }

  var chart = new Chartist.Line('.ct-chart', {
        labels: ['Week 1', '' ,'Week 2', '', 'Week 3', '', 'Week4 4', '', 'Week 5', ],
        series: [
          [20, 18, 21, 17, 15, 12, 9, 5, 0],
          [20, 17, 15, 13, 10, 8, 5, 2, 0]
        ]
      }, {
        low: 0
      });
    
      // Let's put a sequence number aside so we can use it in the event callbacks
      var seq = 0,
        delays = 80,
        durations = 500;
    
      // Once the chart is fully created we reset the sequence
      chart.on('created', function() {
        seq = 0;
      });
    
      // On each drawn element by Chartist we use the Chartist.Svg API to trigger SMIL animations
      chart.on('draw', function(data) {
        seq++;
    
        if(data.type === 'line') {
          // If the drawn element is a line we do a simple opacity fade in. This could also be achieved using CSS3 animations.
          data.element.animate({
            opacity: {
              // The delay when we like to start the animation
              begin: seq * delays + 1000,
              // Duration of the animation
              dur: durations,
              // The value where the animation should start
              from: 0,
              // The value where it should end
              to: 1
            }
          });
        } else if(data.type === 'label' && data.axis === 'x') {
          data.element.animate({
            y: {
              begin: seq * delays,
              dur: durations,
              from: data.y + 100,
              to: data.y,
              // We can specify an easing function from Chartist.Svg.Easing
              easing: 'easeOutQuart'
            }
          });
        } else if(data.type === 'label' && data.axis === 'y') {
          data.element.animate({
            x: {
              begin: seq * delays,
              dur: durations,
              from: data.x - 100,
              to: data.x,
              easing: 'easeOutQuart'
            }
          });
        } else if(data.type === 'point') {
          data.element.animate({
            x1: {
              begin: seq * delays,
              dur: durations,
              from: data.x - 10,
              to: data.x,
              easing: 'easeOutQuart'
            },
            x2: {
              begin: seq * delays,
              dur: durations,
              from: data.x - 10,
              to: data.x,
              easing: 'easeOutQuart'
            },
            opacity: {
              begin: seq * delays,
              dur: durations,
              from: 0,
              to: 1,
              easing: 'easeOutQuart'
            }
          });
        } else if(data.type === 'grid') {
          // Using data.axis we get x or y which we can use to construct our animation definition objects
          var pos1Animation = {
            begin: seq * delays,
            dur: durations,
            from: data[data.axis.units.pos + '1'] - 30,
            to: data[data.axis.units.pos + '1'],
            easing: 'easeOutQuart'
          };
    
          var pos2Animation = {
            begin: seq * delays,
            dur: durations,
            from: data[data.axis.units.pos + '2'] - 100,
            to: data[data.axis.units.pos + '2'],
            easing: 'easeOutQuart'
          };
    
          var animations = {};
          animations[data.axis.units.pos + '1'] = pos1Animation;
          animations[data.axis.units.pos + '2'] = pos2Animation;
          animations['opacity'] = {
            begin: seq * delays,
            dur: durations,
            from: 0,
            to: 1,
            easing: 'easeOutQuart'
          };
    
          data.element.animate(animations);
        }
      });
    
      // For the sake of the example we update the chart every time it's created with a delay of 10 seconds
      chart.on('created', function() {
        if(window.__exampleAnimateTimeout) {
          clearTimeout(window.__exampleAnimateTimeout);
          window.__exampleAnimateTimeout = null;
        }
        window.__exampleAnimateTimeout = setTimeout(chart.update.bind(chart), 12000);
      });

};






//   var graph = makeElement('div', 'graphic');
//   graph.textContent += 'Зелёным цветом отмечены ожидаемые результаты, ';
//   graph.textContent += 'красным - результаты реальной работы.';

//   desc.insertBefore(graph, graphic);
//   // new Chartist.Line('.chart1', {
//   //   labels: ['Week 1', 'Week 2', 'Week 3', 'Week4 4', 'Week 5'],
//   //       series: [
//   //           [20, 15, 10, 5, 0],
//   //           [20, 14, 16, 9, 0],
//   //           // [0, 15, 40, 60, 90]
//   //       ]
//   //   }, {
//   //       fullWidth: true,
//   //       chartPadding: {n
//   //           right: 50
//   //       }
//   // });

//   var gh = makeElement('div', 'github', 'Github: ');
//   var a_gh = document.createElement('a');
//   a_gh.href = 'https://github.com';
//   a_gh.textContent = project.ghLink;
//   gh.appendChild(a_gh);
//   desc.appendChild(gh);


//   var content;
//   var class_status;

//   if (project.flag === 0) { 
//     content = 'Завершён'; 
//     class_status = 'finished';
//   } else if (project.flag === 1) {
//     content = 'В процессе'; 
//     class_status = 'in-proccess';
//   } else { 
//     content = 'Не начат'; 
//     class_status = 'in-future';
//   }

//   document.getElementById('status').innerHTML = content;
//   document.getElementById('status').classList.add(class_status);

//   var color;
//   if (project.level === 'Низкая') { 
//     color = 'easy'; 
//   } else if (project.level === 'Средняя') {
//     color = 'medium';
//   } else { 
//     color = "hard";
//   }

//   document.getElementById('level').innerHTML = project.level;
//   document.getElementById('level').classList.add(color);

//   var jobs = document.querySelector('.list-of-jobs');
//   if (project.flag == 0) {
//     jobs.style.display = 'none';
//   } else {
//     var list = document.querySelector('.list');
//     for (var i = 0; i < project.jobs.length; i++) {
//       var listItem = document.createElement('li');
//       listItem.textContent = '- ' + project.jobs[i];
//       list.appendChild(listItem);
//     }
//   }

//   var tags = document.querySelector('.tags');
//   for (var i = 0; i < project.tags.length; i++) {
//     var litem = document.createElement('li');
//     litem.textContent = project.tags[i];
//     tags.appendChild(litem);
//   }

//   var chart = new Chartist.Line('.ct-chart', {
//     labels: ['Week 1', '' ,'Week 2', '', 'Week 3', '', 'Week4 4', '', 'Week 5', ],
//     series: [
//       [20, 18, 21, 17, 15, 12, 9, 5, 0],
//       [20, 17, 15, 13, 10, 8, 5, 2, 0]
//     ]
//   }, {
//     low: 0
//   });

//   // Let's put a sequence number aside so we can use it in the event callbacks
//   var seq = 0,
//     delays = 80,
//     durations = 500;

//   // Once the chart is fully created we reset the sequence
//   chart.on('created', function() {
//     seq = 0;
//   });

//   // On each drawn element by Chartist we use the Chartist.Svg API to trigger SMIL animations
//   chart.on('draw', function(data) {
//     seq++;

//     if(data.type === 'line') {
//       // If the drawn element is a line we do a simple opacity fade in. This could also be achieved using CSS3 animations.
//       data.element.animate({
//         opacity: {
//           // The delay when we like to start the animation
//           begin: seq * delays + 1000,
//           // Duration of the animation
//           dur: durations,
//           // The value where the animation should start
//           from: 0,
//           // The value where it should end
//           to: 1
//         }
//       });
//     } else if(data.type === 'label' && data.axis === 'x') {
//       data.element.animate({
//         y: {
//           begin: seq * delays,
//           dur: durations,
//           from: data.y + 100,
//           to: data.y,
//           // We can specify an easing function from Chartist.Svg.Easing
//           easing: 'easeOutQuart'
//         }
//       });
//     } else if(data.type === 'label' && data.axis === 'y') {
//       data.element.animate({
//         x: {
//           begin: seq * delays,
//           dur: durations,
//           from: data.x - 100,
//           to: data.x,
//           easing: 'easeOutQuart'
//         }
//       });
//     } else if(data.type === 'point') {
//       data.element.animate({
//         x1: {
//           begin: seq * delays,
//           dur: durations,
//           from: data.x - 10,
//           to: data.x,
//           easing: 'easeOutQuart'
//         },
//         x2: {
//           begin: seq * delays,
//           dur: durations,
//           from: data.x - 10,
//           to: data.x,
//           easing: 'easeOutQuart'
//         },
//         opacity: {
//           begin: seq * delays,
//           dur: durations,
//           from: 0,
//           to: 1,
//           easing: 'easeOutQuart'
//         }
//       });
//     } else if(data.type === 'grid') {
//       // Using data.axis we get x or y which we can use to construct our animation definition objects
//       var pos1Animation = {
//         begin: seq * delays,
//         dur: durations,
//         from: data[data.axis.units.pos + '1'] - 30,
//         to: data[data.axis.units.pos + '1'],
//         easing: 'easeOutQuart'
//       };

//       var pos2Animation = {
//         begin: seq * delays,
//         dur: durations,
//         from: data[data.axis.units.pos + '2'] - 100,
//         to: data[data.axis.units.pos + '2'],
//         easing: 'easeOutQuart'
//       };

//       var animations = {};
//       animations[data.axis.units.pos + '1'] = pos1Animation;
//       animations[data.axis.units.pos + '2'] = pos2Animation;
//       animations['opacity'] = {
//         begin: seq * delays,
//         dur: durations,
//         from: 0,
//         to: 1,
//         easing: 'easeOutQuart'
//       };

//       data.element.animate(animations);
//     }
//   });

//   // For the sake of the example we update the chart every time it's created with a delay of 10 seconds
//   chart.on('created', function() {
//     if(window.__exampleAnimateTimeout) {
//       clearTimeout(window.__exampleAnimateTimeout);
//       window.__exampleAnimateTimeout = null;
//     }
//     window.__exampleAnimateTimeout = setTimeout(chart.update.bind(chart), 12000);
//   });
// };

// // run();

// var che = function(i) {
//   alert(i);
// }
