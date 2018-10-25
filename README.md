
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