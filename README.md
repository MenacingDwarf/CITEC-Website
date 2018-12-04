## REST API (application/json)

### HEROKU APP API URL: https://agile-depths-28542.herokuapp.com/api

#### Authorization:

Для любых запросов слать Authorization: 8006590e56914f96bb2a0d67a998a234 


#### Endpoints:
##### (**) - обязательный параметр

=======

#####Получение общей информации (см. структуру объекта "Общая информация"): 

<pre>GET /projects/info</pre>

#####Ответ:

Кол-во проектов (total), кол-во активных проектов (active)

#####Пример:

Запрос:

<pre>
/projects/info
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

<pre>GET /projects</pre>

#####Параметры:

* limit (**) - максимальное число возвращаемых проектов
* offset (**) - "отступ" в глобальном списке проектов
* metrics - список "мгновенных" метрик (присылается только последнее актуальное значение)

#####Ответ:

Список проектов, отсортированный по дате начала (новый - старый).

#####Пример:

Запрос: <pre>/projects?limit=5\&offset=0\&metrics=random, another</pre>

Ответ:

<pre>
[
    {
        "id": 3,
        "name": "Project #4",
        "client": "Client #4",
        "status": 0,
        "description": "This is #4 project",
        "githubRepo": "/ocornut/imgui",
        "startedAt": "2010-11-10T21:00:00.000+0000",
        "closedAt": null,
        "tags": [],
        "metrics": []
    },
    {
        "id": 4,
        "name": "Project #3",
        "client": "Client #3",
        "status": 1,
        "description": "This is #3 project",
        "githubRepo": "/aws-amplify/amplify-js",
        "startedAt": "2009-11-10T21:00:00.000+0000",
        "closedAt": null,
        "tags": [],
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
    },
    {
        "id": 1,
        "name": "Project #2",
        "client": "Client #2",
        "status": 1,
        "description": "This is #2 project",
        "githubRepo": "/SilverTiger/lwjgl3-tutorial",
        "startedAt": "2008-11-10T21:00:00.000+0000",
        "closedAt": null,
        "tags": [],
        "metrics": [
            {
                "type": "another",
                "value": 0.8923983
            },
            {
                "type": "random",
                "value": 0.39174023
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
        "startedAt": "2007-11-10T21:00:00.000+0000",
        "closedAt": null,
        "tags": [
            "birdy",
            "wings"
        ],
        "metrics": [
            {
                "type": "another",
                "value": 0.010045757
            },
            {
                "type": "random",
                "value": 0.046086412
            }
        ]
    }
]
</pre>

========

#####Получение метрик для конкретного проекта (см. структуру объекта "Метрики"):

<pre>GET /metrics</pre>

#####Параметры:

* id (**) - id проекта 
* from (**) - начальная дата (пример: 2018-10-04 10:32:12)
* until (**) - конечная дата (пример: 2018-10-04 10:32:12)
* interval - "разница" между ближайшими группами метрик в минутах. 
Разница примерная, реальное время получения группы метрик можно найти в поле "timestamp".

#####Ответ:

Отсортированные по дате (новая - старая) группы метрик. 


#####Пример:

Запрос: <pre>/metrics?projectId=4\&from=2012-09-17 18:47:52\&until=2019-09-17 18:47:52\&interval=1</pre>

Ответ (я только залил, обновления раз в час, так что пока 1 запись):

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