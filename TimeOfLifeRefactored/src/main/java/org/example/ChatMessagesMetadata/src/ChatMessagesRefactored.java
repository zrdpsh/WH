//import java.util.ArrayList;
//import java.util.Optional;
//
//
///**
// * Первоначальный метод - loadStoriesForMessages внутри класса ChatMessages
// * ЦС первоначального метода - 9
// *
// * Способы снижения ЦС:
// * 1. Избавление от if-else с помощью ad-hoc полиморфизма,
// * интерфейс StoryHandler, классы-наследники для каждой из проверок
// * 2. Избавление от проверок на null
// *
// * переписанный метод - loadStoriesForMessages внутри класса ChatMessagesRefactored
// * дополнительный файл - StoryHandlerFactory
// * ЦС нового кода - 2
// */
//
//
//public class ChatMessagesRefactored {
//
//    final ChatActivity chatActivity;
//    private ArrayList<MessageObject> reactionsToCheck = new ArrayList<>(10);
//    private ArrayList<MessageObject> extendedMediaToCheck = new ArrayList<>(10);
//    private ArrayList<MessageObject> storiesToCheck = new ArrayList<>(10);
//
//    ArrayList<Integer> reactionsRequests = new ArrayList<>();
//    ArrayList<Integer> extendedMediaRequests = new ArrayList<>();
//
//
//    public ChatMessagesRefactored(ChatActivity chatActivity) {
//        this.chatActivity = chatActivity;
//    }
//
//    private void loadStoriesForMessages(long dialogId, ArrayList<MessageObject> visibleObjects) {
//        if (visibleObjects.isEmpty()) {
//            return;
//        }
//        StoryHandlerFactory storyHandlerFactory = new StoryHandlerFactory();
//
//        visibleObjects.stream()
//                .forEach(messageObject -> {
//                    StoryHandler handler = storyHandlerFactory.getHandler(messageObject.type);
//
//                    StringBuilder storyData = new StringBuilder();
//                    handler.handleStory(messageObject, storyData);
//
//                    sendStoryRequest(storyData.toString(), messageObject);
//                });
//
//        cleanupOldRequests();
//    }
//
//    private void sendStoryRequest(String storyData, MessageObject messageObject) {
//        TL_stories.TL_stories_getStoriesByID req = new TL_stories.TL_stories_getStoriesByID();
//        long storyDialogId = storyItem.dialogId;
//        req.peer = chatActivity.getMessagesController().getInputPeer(messageObject.messageOwner.media.user_id);
//        req.id.add(Integer.parseInt(storyData));
//
//        int reqId = chatActivity.getConnectionsManager().sendRequest(req, (response, error) -> {
//            TL_stories.StoryItem newStoryItem = Optional.ofNullable(response)
//                    .map(resp -> ((TL_stories.TL_stories_stories) resp).stories.get(0))
//                    .orElseGet(TL_stories.TL_storyItemDeleted::new);
//
//            newStoryItem.lastUpdateTime = System.currentTimeMillis();
//            newStoryItem.id = storyId;
//            updateUIWithStory(newStoryItem, messageObject, storyDialogId);
//        });
//        extendedMediaRequests.add(reqId);
//    }
//
//    private void cleanupOldRequests() {
//        if (extendedMediaRequests.size() > 10) {
//            chatActivity.getConnectionsManager().cancelRequest(extendedMediaRequests.remove(0), false);
//        }
//    }
//
//    private void updateUIWithStory(TL_stories.StoryItem storyItem, MessageObject messageObject, long storyDialogId) {
//        boolean wasExpired = messageObject.isExpiredStory();
//        StoriesStorage.applyStory(chatActivity.getCurrentAccount(), storyDialogId, messageObject, storyItem);
//        ArrayList<MessageObject> messageObjects = new ArrayList<>();
//        messageObject.forceUpdate = true;
//        messageObjects.add(messageObject);
//        chatActivity.getMessagesStorage().getStorageQueue().postRunnable(() -> {
//            chatActivity.getMessagesController().getStoriesController().getStoriesStorage().updateMessagesWithStories(messageObjects);
//        });
//        if (!wasExpired && messageObject.isExpiredStory() && messageObject.type == MessageObject.TYPE_STORY_MENTION) {
//            chatActivity.updateMessages(messageObjects, true);
//            return;
//        }
//        chatActivity.updateMessages(messageObjects, false);
//    }
//}
