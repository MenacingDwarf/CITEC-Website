
var dates = [];
var metrics_chart = [];

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
    url: "https://agile-depths-28542.herokuapp.com/api/public/projects?limit=10&offset=0&metrics=progress,countclosed",
    headers: {
      'Authorization': '8006590e56914f96bb2a0d67a998a234'
    },
    success: function(data) {
        project = find(data);
        var date = project.startedAt.substring(0, 10) + " " + project.startedAt.substring(11, 19);
        var request = "https://agile-depths-28542.herokuapp.com/api/public/metrics?projectId=";
        request += project.id;
        request += "&from=";
        request += date;
        request += "&until=2019-09-17 18:47:52&interval=60";
        // console.log(request);
        $.ajax({
          type: "GET",
          
          url: request,
          dataType: "json",
          headers: {
            'Authorization': '8006590e56914f96bb2a0d67a998a234'
          },
          success: function(met) {
            createDates(met);
            createClosedCount(met);
            console.log(met);
            console.log(metrics_chart);
            run(project);
          }
        });
    }
  });
});

var createDates = function(metrics_graph) {
  for (let i = 0; i < metrics_graph.length; i++) {
    var element = metrics_graph[i].timestamp.substring(0,10) + " " + metrics_graph[i].timestamp.substring(11,16);
    dates.push(element);
  }
}

var createClosedCount = function(metrics_graph) {
  for (let i = 0; i < metrics_graph.length; i++) {
    var element;
    if (metrics_graph[i].metrics[0].type == 'countclosed') {
      element = metrics_graph[i].metrics[0].value;
    } else {
      element = metrics_graph[i].metrics[1].value;
    }
    metrics_chart.push(element);
  }
}

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
  // desc.appendChild('graph');

  var gh = makeElement('div', 'github', 'Github: ');
  var a_gh = document.createElement('a');
  a_gh.href = 'https://github.com/' + project.githubRepo;
  a_gh.textContent = 'Ссылка на репозиторий';
  gh.appendChild(a_gh);
  desc.appendChild(gh);

  var content;
  var class_status;

  var jobs = document.querySelector('.list-of-jobs');

  if (project.metrics[0].value == 1) { 
    content = 'Завершён'; 
    class_status = 'finished';
  } else if (project.metrics[0].value == 0) {
    content = 'Не начат'; 
    class_status = 'in-future';
  } else { 
    content = 'В процессе'; 
    class_status = 'in-proccess';
  }
  

  document.getElementById('status').innerHTML = content;
  document.getElementById('status').classList.add(class_status);

  var color;
  var project_level;
  if (project.difficulty === 'Низкий') { 
    color = 'easy'; 
    project_level = 'Низкая';
  } else if (project.difficulty === 'Средний') {
    color = 'medium';
    project_level = 'Средняя';
  } else { 
    color = "hard";
    project_level = 'Высокая';
  }

  document.getElementById('level').innerHTML = project_level;
  document.getElementById('level').classList.add(color);
  
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
        labels: dates,
        series: [
          metrics_chart
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

