**1.1 Методы только для тестов**
Таких удалось найти немного, один из них: 
```cpp
#if DCHECK_IS_ON() || !defined(NDEBUG)
bool WorkQueueSets::ContainsWorkQueueForTest(
    const WorkQueue* work_queue) const {
  EnqueueOrder enqueue_order;
  bool has_enqueue_order = work_queue->GetFrontTaskEnqueueOrder(&enqueue_order);

  for (const base::internal::IntrusiveHeap<OldestTaskEnqueueOrder>& heap :
       work_queue_heaps_) {
    for (const OldestTaskEnqueueOrder& heap_value_pair : heap) {
      if (heap_value_pair.value == work_queue) {
        DCHECK(has_enqueue_order);
        DCHECK_EQ(heap_value_pair.key, enqueue_order);
        DCHECK_EQ(this, work_queue->work_queue_sets());
        return true;
      }
    }
  }

  if (work_queue->work_queue_sets() == this) {
    DCHECK(!has_enqueue_order);
    return true;
  }

  return false;
}
#endif
```
Решение - перенести проверку в тесты, замокировав поведение оригинального класса. Как сделать это в C++, я, к сожалению, не успел разобраться.

**1.2 Цепочки методов**
В наиболее общем виде такой код должен выглядеть как матрёшка, вроде этого:
```
    public void sendMessage(String messageText, String id) {
        validateMessage(messageText);
    }

    private void validateMessage(String messageText) {
        if (messageText == null || messageText.isEmpty()) {
            return;
        }
        checkUserRecipient(messageText);
    }

    private void checkUserRecipient(String messageText) {
        String recipientId = extractRecipient(messageText);
        if (recipientId == null) {
            return;
        }
        prepareMessage(messageText, recipientId);
    // rest of code
    }
```
общее решение - выделить основной объект, над которым совершаются манипуляции, и передавать в качественно аругмента именно его:

```
public void sendMessage(String messageText, String id) {
    if (!isMessageValid(messageText)) {
        return;
    }

    String recipient = extractRecipient(messageText);
    if (recipient == null) {
        return;
    }
    // rest of code
}

    private boolean isMessageValid(String message) {
        return message != null && !message.isEmpty();
    }

    private String extractRecipient(String message) {
        return "someText";
    }
}

```

**1.3 Слишком много параметров.**

Методы с длинным списком параметров многократно встречаются в коде тг - 6, 7, 8, даже 11 аргументов. Иногда это оправданно - там, где нужно передавать координаты для графики, или сознательно пользуются перегрузкой, или авторы вынуждены использовать методы, доставшиеся от ОС, а иногда всё вместе. В этих случаях рефакторинг, кажется, не нужен. 
Кроме того, такие методы чаще очень громоздкие, и количество аргументов - следствие более серьезных проблем.

Пример попроще:
```
public void itemClick(UItem item, View view, int position, float x, float y) {
        if (item.instanceOf(Address2View.Factory.class)) {
            String query = item.text.toString();
            if (onQueryClick != null) {
                onQueryClick.run(query);
            }
        } else if (item.instanceOf(BookmarkView.Factory.class)) {
            if (onURLClick != null) {
                try {
                    onURLClick.run(getLink((MessageObject) item.object2));
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }
```
Здесь можно было бы переместить параметры внутрь заранее созданного объекта, и передавать его. Длинное объявление, правда, никуда не исчезло.
```
public class clickContext {
    public UItem item;
    public View view;
    public int position;
    public float x;
    public float y;

    public ClickContext(UItem item, View view, int position, float x, float y) {
        this.item = item;
        this.view = view;
        this.position = position;
        this.x = x;
        this.y = y;
    }
}
```
Но именно в этом случае поиск по кодовой базе ничего не дал, и можно спокойно оставить только первый параметр, остальные не искользуются, а сам метод ничего не переодпределяет, сигнатуру можно не сохранять.
```
public void itemClick(UItem item) {
        if (item.instanceOf(Address2View.Factory.class)) {
            String query = item.text.toString();
            if (onQueryClick != null) {
                onQueryClick.run(query);
            }
            return;    
        }
        if (item.instanceOf(BookmarkView.Factory.class)) {
            if (onURLClick != null) {
                try {
                    onURLClick.run(getLink((MessageObject) item.object2));
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }
```

**1.4 Странные решения.**

В следующем коде при получении messageId либо возвращается результат с избыточным числом параметров ("cell.getMessage().getId()"), либо использует getMessageId(), получая айди напрямую, но в этом случае возникает рассогласованность - идентичную задачу выполяют разные методы.

```java
        private void saveScrollPosition() {
           for (int k = 0; k < mediaPages.length; k++) {
               RecyclerListView listView = mediaPages[k].listView;
               if (listView != null) {
                   int messageId = 0;
                   int offset = 0;
                   for (int i = 0; i < listView.getChildCount(); i++) {
                       View child = listView.getChildAt(i);
                       if (child instanceof SharedPhotoVideoCell2) {
                           SharedPhotoVideoCell2 cell = (SharedPhotoVideoCell2) child;
                           messageId = cell.getMessageId();
                           offset = cell.getTop();
                       }
                       if (child instanceof SharedDocumentCell) {
                           SharedDocumentCell cell = (SharedDocumentCell) child;
                           messageId = cell.getMessage().getId();
                           offset = cell.getTop();
                       }
                       if (child instanceof SharedAudioCell) {
                           SharedAudioCell cell = (SharedAudioCell) child;
                           messageId = cell.getMessage().getId();
                           offset = cell.getTop();
                       }
                       if (messageId != 0) {
                           break;
                       }
                   }
                   if (messageId != 0) {
                       int index = -1, position = -1;
                       final int type = mediaPages[k].selectedType;
                       if (type == TAB_STORIES || type == TAB_ARCHIVED_STORIES) {
                           SharedMediaLayout.StoriesAdapter adapter = type == TAB_STORIES ? storiesAdapter : archivedStoriesAdapter;
                           if (adapter.storiesList != null) {
                               for (int i = 0; i < adapter.storiesList.messageObjects.size(); ++i) {
                                   if (messageId == adapter.storiesList.messageObjects.get(i).getId()) {
                                       index = i;
                                       break;
                                   }
                               }
                           }
                           position = index;
                       } else if (type >= 0 && type < sharedMediaData.length) {
                           for (int i = 0; i < sharedMediaData[type].messages.size(); i++) {
                               if (messageId == sharedMediaData[type].messages.get(i).getId()) {
                                   index = i;
                                   break;
                               }
                           }
                           position = sharedMediaData[type].startOffset + index;
                       } else {
                           continue;
                       }
                       if (index >= 0) {
                           ((LinearLayoutManager) listView.getLayoutManager()).scrollToPositionWithOffset(position, -mediaPages[k].listView.getPaddingTop() + offset);
                           if (photoVideoChangeColumnsAnimation) {
                               mediaPages[k].animationSupportingLayoutManager.scrollToPositionWithOffset(position, -mediaPages[k].listView.getPaddingTop() + offset);
                           }
                       }
                   }
               }
           }
        }
```
решение - создать общий интерфейс для всех подобных ячеек с данными, либо, как минимум создать новый метод getDocumentId и у sharedDocumentCell, аналогичный такому же у SharedPhotoVideoCell2  - примерно следующего вида:
```
    public int getMessageId() {
        return currentMessageObject != null ? currentMessageObject.getId() : 0;
    }
```

**1.5 Методов, передающих избыточн много параметров, найти не смог**

