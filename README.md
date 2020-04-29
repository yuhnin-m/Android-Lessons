# Модуль Android разработка 2020
## Задание №1. Создать проект на GitHub
В проекте на GitHub добавить преподавателя как collaborator
Создать ветку от master
В этой ветке сгенерировать проект с пустой активностью
Сделать commit/push
Оформить Pull Request из ветки в master. В качестве аппрувера установить преподавателя

## Задание №2. Single Activity приложение списка контактов

Необходимо реализовать Single Activity приложение списка контактов(Пока только верстка и навигация)

### Составные компоненты(Смотрите рисунок во вложении):
#### MainActivity - главная и единственная активность в приложении
#### ContactListFragment - фрагмент со списком контактов
 - Короткая карточка контакта
 - Фотография контакта
 - Имя контакта
 - Телефон контакта

При клике пользователя на карточку контакта происходит переход на фрагмент деталей контактов. Необходимо в аргументах передавать ID выбранного контакта
#### ContactDetailsFragment
 - Фотография контакта
 - Имя контакта
 - Телефон контакта
 - 2-й телефон
 - e-mail контакта
 - 2-й e-mail контакта
 - Описание контакта
 
На экране списка контактов пока достаточно реализовать одну карточку контакта
### Результат
| Главный экран | Детали контакта |
| ------ | ------ |
| ![Детали](https://i.imgur.com/wzEXaTm.jpg) |![Главный экран](https://i.imgur.com/rhFKsrB.jpg)
  