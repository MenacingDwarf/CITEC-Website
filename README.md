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
        "id": 1,
        "name": "Анализ данных онлайн курсов",
        "client": "СПбГУ, ЦРЭОР",
        "status": 1,
        "description": "На основе данных материалов и действий учащихся онлайн-курсов выявить показатели, определяющие их эффективность. Эффективность курса состоит из двух компонент: качество образовательных материалов и коммерческий потенциал. Данные являются отчётами и логами образовательных платформ “Coursera” и “Открытое образование”.",
        "githubRepo": "/SPbSUPractiseTeam/Quality_rating_for_SPbSU_courses",
        "difficulty": "Высокий",
        "startedAt": "2018-09-15T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "data"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": 0.72727275
            },
            {
                "type": "countclosed",
                "value": 11
            }
        ]
    },
    {
        "id": 2,
        "name": "Аналитика данных электронного расписания",
        "client": "СПбГУ, УСИТ",
        "status": 1,
        "description": "На основе данных электронного расписания и свободного ПО с открытым исходным кодом создать сервис кэширования данных и аналитическое Web приложение. Примеры аналитических срезов: занятость аудиторий, занятость преподавателей, занятость студентов, доступные аудитории по заданным критериям.",
        "githubRepo": "/spbu-schedule/backend",
        "difficulty": "Средний",
        "startedAt": "2018-09-15T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "data"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": 1
            },
            {
                "type": "countclosed",
                "value": 0
            }
        ]
    },
    {
        "id": 3,
        "name": "Анализ текстов судебных решений",
        "client": "Dentons",
        "status": 1,
        "description": "Обработка текстов судебных решений, извлечение ссылок на нормативные и законодательные акты, статистика.",
        "githubRepo": "/robot-lab/judyst-main-web-service",
        "difficulty": "Высокий",
        "startedAt": "2018-09-15T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "text",
            "data"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": 0.50769234
            },
            {
                "type": "countclosed",
                "value": 65
            }
        ]
    },
    {
        "id": 4,
        "name": "Портфолио студентов",
        "client": "СПбГУ, УРМ",
        "status": 1,
        "description": "Web приложения для ввода и согласования студентами и сотрудниками УРМ сведений о достижениях студентов. Прототип функциональности по расчёту повышенной степендии на основе данных портфолио.",
        "githubRepo": "/am-cp-edu-project/portfolio",
        "difficulty": "Высокий",
        "startedAt": "2018-09-15T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "dont give it up!"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": 1
            },
            {
                "type": "countclosed",
                "value": 0
            }
        ]
    },
    {
        "id": 5,
        "name": "Сайт ЦИТИК",
        "client": "СПбГУ, УУ",
        "status": 1,
        "description": "Простой сайт с информацией о центре, выполненных и предстоящих работах, с инструментами коммуникации.",
        "githubRepo": "/MenacingDwarf/CITEC-Website",
        "difficulty": "Средний",
        "startedAt": "2018-09-15T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "citec",
            "web"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": 0.80952376
            },
            {
                "type": "countclosed",
                "value": 42
            }
        ]
    },
    {
        "id": 6,
        "name": "Сайт Медиацентра",
        "client": "СПбГУ, Медиацентр",
        "status": 1,
        "description": "Разработать сайт Медиацентра СПбГУ в соответствии с требованиями, предъявленными заказчиком",
        "githubRepo": "/valery-kirichenko/mediacenter",
        "difficulty": "Средний",
        "startedAt": "2018-09-15T00:00:00.000+0300",
        "closedAt": null,
        "tags": [
            "web",
            "media"
        ],
        "metrics": [
            {
                "type": "progress",
                "value": 0.14705883
            },
            {
                "type": "countclosed",
                "value": 17
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