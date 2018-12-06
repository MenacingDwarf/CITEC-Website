## REST API (application/json)

### HEROKU APP API URL: https://agile-depths-28542.herokuapp.com/api


#### Метрики:

progress - общий прогресс

countclosed - число закрытых issues (?)

#### Authorization:

Для любых public запросов слать Authorization: 8006590e56914f96bb2a0d67a998a234.

Для любых private запросов Basic Authorization: username = admin, password = admin123


#### Endpoints:
##### (**) - обязательный параметр

=======

#####Получение общей информации (см. структуру объекта "Общая информация"): 

<pre>GET public/projects/info</pre>

#####Ответ:

Кол-во проектов (total), кол-во активных проектов (active)

#####Пример:

Запрос:

<pre>
public/projects/info
</pre>

Ответ:

<pre>
{
    "total": 4,
    "active": 3
}
</pre>

=======

#####Получение списка проектов (см. структуру объекта "Проект"):

<pre>GET public/projects</pre>

#####Параметры:

* limit (**) - максимальное число возвращаемых проектов
* offset (**) - "отступ" в глобальном списке проектов
* metrics - список "мгновенных" метрик (присылается только последнее актуальное значение)

#####Ответ:

Список проектов, отсортированный по дате начала (новый - старый).

#####Пример:

Запрос: <pre>public/projects?limit=5\&offset=0\&metrics=random, another</pre>

Ответ:

<pre>
[
    {
        "id": 5,
        "name": "Project #5",
        "client": "Client #5",
        "status": 1,
        "description": "This is #5 project",
        "githubRepo": "/mozilla/addons-server",
        "difficulty": "medium",
        "startedAt": "2011-11-11T00:00:00.000+0400",
        "closedAt": null,
        "tags": [],
        "metrics": [
            {
                "type": "progress",
                "value": 0.9247605
            },
            {
                "type": "countclosed",
                "value": 1065
            }
        ]
    },
    {
        "id": 3,
        "name": "Project #4",
        "client": "Client #4",
        "status": 0,
        "description": "This is #4 project",
        "githubRepo": "/ocornut/imgui",
        "difficulty": "very hard",
        "startedAt": "2010-11-11T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "imgui"
        ],
        "metrics": []
    },
    {
        "id": 4,
        "name": "Project #3",
        "client": "Client #3",
        "status": 1,
        "description": "This is #3 project",
        "githubRepo": "/aws-amplify/amplify-js",
        "difficulty": "easy peasy",
        "startedAt": "2009-11-11T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "aws",
            "amazon"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": 0.9107468
            },
            {
                "type": "countclosed",
                "value": 61
            }
        ]
    },
    {
        "id": 1,
        "name": "Project #2",
        "client": "Client #2",
        "status": 1,
        "description": "This is #2 project",
        "githubRepo": "/SilverTiger/lwjgl3-tutorial",
        "difficulty": "hard",
        "startedAt": "2008-11-11T00:00:00.000+0300",
        "closedAt": null,
        "tags": [],
        "metrics": [
            {
                "type": "progress",
                "value": 0.5
            },
            {
                "type": "countclosed",
                "value": 2
            }
        ]
    },
    {
        "id": 2,
        "name": "Project #1",
        "client": "Client #1",
        "status": 1,
        "description": "This is #1 project",
        "githubRepo": "/SilverTiger/lwjgl3",
        "difficulty": "easy",
        "startedAt": "2007-11-11T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "java",
            "lwjgl"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": "0"
            },
            {
                "type": "countclosed",
                "value": 0
            }
        ]
    }
]
</pre>

========

#####Получение метрик для конкретного проекта (см. структуру объекта "Метрики"):

<pre>GET public/metrics</pre>

#####Параметры:

* id (**) - id проекта 
* from (**) - начальная дата (пример: 2018-10-04 10:32:12)
* until (**) - конечная дата (пример: 2018-10-04 10:32:12)
* interval - "разница" между ближайшими группами метрик в минутах. 
Разница примерная, реальное время получения группы метрик можно найти в поле "timestamp".

#####Ответ:

Отсортированные по дате (новая - старая) группы метрик. 


#####Пример:

Запрос: <pre>public/metrics?projectId=4\&from=2012-09-17 18:47:52\&until=2019-09-17 18:47:52\&interval=1</pre>

Ответ:

<pre>

[
    {
        "timestamp": "2018-12-04T15:37:38.340+0000",
        "metrics": [
            {
                "type": "another",
                "value": 0.91372854
            },
            {
                "type": "random",
                "value": 0.5974828
            }
        ]
    }
]

</pre>

========

#####Добавить проект:

<pre>POST private/projects</pre>

#####Параметры в json:

* name (**)
* client (**)
* description (**)
* githubRepo (**) - пример: /heroku/java-getting-started
* difficulty (**) - пример: hard
* status (**)
* startedAt (**) - пример: 2012-09-17
* closedAt
* tags

========

#####Изменить проект:

<pre>PUT private/projects</pre>

#####Параметры в json:

* id (**)
* name
* client 
* description
* difficulty
* status 
* startedAt
* closetAt
* tags

========

#####Удалить проект:

<pre>DELETE private/projects</pre>

#####Параметры:

* id (**)

========

#####Получение информации о приложении 

<pre>GET private/monitoring/{name}</pre>

#####Пример:

Запрос: <pre>private/monitoring/health</pre>

Ответ:

<pre>
{
    "status": "UP",
    "details": {
        "db": {
            "status": "UP",
            "details": {
                "database": "PostgreSQL",
                "hello": 1
            }
        },
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 998776471552,
                "free": 773159235584,
                "threshold": 10485760
            }
        }
    }
}
</pre>

