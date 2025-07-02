# DiamondReports 💎 - Супер-удобная система модерации для Minecraft

Плагин, который превращает обработку жалоб в удовольствие! Красивое меню, защита от дурака на каждом шагу и полная интеграция с Discord и LiteBans — всё для комфортной работы модераторов 😎

## Что умеет? 🔥

- 📝 **Создание жалоб** - `/report <ник> [причина]`
- 🖥️ **Меню для модераторов** - `/reports` открывает GUI Menu с головами игроков (можно сортировать разными способами, перелистывать на другие страницы, получать подробнейшую информацию о репорте, забирать репорт себе)
- 🔔 **Авто-оповещения** - Модераторы видят новые жалобы сразу в чате
- 🔄 **Умная синхронизация** - При бане через LiteBans репорты удаляются автоматом
- 📨 **Discord-трансляция** - ВСЕ действия с репортом логируются в Discord посредством вебхука (например когда модер берёт репорт, информация об этом идёт в Discord)
- 🔍 **Удобная сортировка** - Новые сверху/старые сверху и т.д.

## Как это работает? ⚙️

### 🧩 Динамические менюшки 
Кастомные GUI с пагинацией и сортировкой - [Spigot Inventory API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/package-summary.html)

### 🤖 Discord-интеграция 
Асинхронная отправка вебхуков без лагов - [Discord Webhooks](https://discord.com/developers/docs/resources/webhook)

### 🛡️ Защита от дурака 
Проверки на каждом шагу:
- Нельзя взять два репорта сразу
- Нельзя взаимодействовать с чужими репортами (например другой модератор уже взял один из репортов а другой в это же время тоже захотел его взять)
- Автоматическая очистка неактивных репортов

### 👤 Головы игроков 
Используем SkullMeta для отображения скинов в меню - [SkullMeta Docs](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/meta/SkullMeta.html)

## Технологии под капотом 🛠️

- **Spigot API 1.17+** - Основа основ ([документация](https://www.spigotmc.org/wiki/spigot/))
- **LiteBans API** - Мощная интеграция с системой банов ([исходники](https://github.com/ruany/LiteBans))
- **Java SimpleDateFormat** - Красивое форматирование дат ([доки](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html))

## Структура проекта 📂

```
DiamondReports/
├── src/
│   └── main/
│       ├── java/me/gusandr/diamondreports/
│       │   ├── Plugin.java          # Сердце плагина
│       │   ├── util/Utils.java       # Помощник для создания предметов
│       │   ├── menu/                 # Всё про волшебное меню
│       │   │   ├── ReportMenu.java    # Логика меню
│       │   │   ├── item/ItemReport.java # Карточка репорта
│       │   │   ├── event/MenuEvent.java # Клики в меню
│       │   │   └── command/ReportMenuCMD.java # Открытие меню
│       │   └── report/command/       # Команды репортов
│       │       ├── ReportCMD.java      # Создание репорта
│       │       └── ReportCompleter.java 
│       └── resources/
│           ├── config.yml   # Настройки текстов и форматов
│           └── plugin.yml   # Инфо о плагине
└── pom.xml                  # Сборка проекта
```

### Пояснения 🗺️
- `menu/` - Здесь живёт вся магия интерфейса
- `report/command/` - Обработка команд игроков
- `resources/` - Конфиги, которые можно кастомить

## Настройка ⚙️

```yaml
# Как будет выглядеть карточка игрока
item-report:
  name: "&2&l%suspectPlayer"  # Заголовок
  lore:                        # Описание
    - "&6Отправил %whoSendReport"
    - "&dДата: &n%date&f"
    - "&cОписание: &l%moreInfo"
    - "Кликни что бы начать взаимодействие!"
    - "&cРепорт занят? &l%complaintTaken"

# Формат даты и времени
date:
  format: "dd.MM.yyyy HH:mm:ss"

# Куда слать Discord-уведомления
discord:
  webhooks: "https://discord.com/api/webhooks/xxx/xxxxx"

# Системные сообщения
text:
  message:
    new-report-send: "&cМодераторы! Новый репорт! Смотрите /reports"
    it-worked: "&aВсё получилось!"
```

## Автоочистка при банах ⚡

При бане через LiteBans репорт автоматом удаляется из системы:

```java
litebans.api.Events.get().register(entry -> {
    if (entry.getType().equals("ban")) {
        UUID bannedPlayer = UUID.fromString(entry.getUuid());
        ReportMenu.deleteReport(bannedPlayer);
    }
});
```

---

> Сделано с ❤️ для удобной модерации.
