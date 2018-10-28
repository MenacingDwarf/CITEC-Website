## Структура объекта "Проект" на примере

    {
      "id" : "ebedd3ac-12df-4c99-9ef9-6b16933b1cbe",
      "name" : "Project 1",
      "client" : "Client 1",
      "description" : "Description 1",
    
      "groups" : [
        {
          "timestamp" : "yyyy.MM.dd.HH.mm.ss",
          "metrics" : [
            {"type" : 0, "value" : 0.247},
            {"type" : 1, "value" : 0.875},
            {"type" : 2, "value" : 0.224}
          ]
        },
    
        {
          "timestamp" : "yyyy.MM.dd.HH.mm.ss",
          "metrics" : [
            {"type" : 0, "value" : 0.275},
            {"type" : 1, "value" : 0.8474},
            {"type" : 2, "value" : 0.422}
          ]
        },
    
        {
          "timestamp" : "yyyy.MM.dd.HH.mm.ss",
          "metrics" : [
            {"type" : 0, "value" : 0.17},
            {"type" : 1, "value" : 0.4778},
            {"type" : 2, "value" : 0.789}
          ]
        }
      ]
    }


## REST API (application/json)

Добавление проекта:

POST /projects 

Json body: 

* githubRepo - строка в формате user/repo, максимум 255 (пример: MenacingDwarf/CITEC-Website)
* name - строка, максимум 255
* ... всё что посчитаем нужным, самое главное - githubRepo

Результат в случае успеха: 200 OK

=======

Получение общей информации:

GET /projects/info

Результат (Json):

* total - общее число проектов
* ... всё что посчитаем нужным
   

=======

Получение списка проектов:

GET /projects

(В процессе)