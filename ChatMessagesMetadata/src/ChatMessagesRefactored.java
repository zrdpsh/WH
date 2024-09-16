import java.util.ArrayList;

public class ChatMessagesRefactored {

    final ChatActivity chatActivity;
    private ArrayList<MessageObject> reactionsToCheck = new ArrayList<>(10);
    private ArrayList<MessageObject> extendedMediaToCheck = new ArrayList<>(10);
    private ArrayList<MessageObject> storiesToCheck = new ArrayList<>(10);

    ArrayList<Integer> reactionsRequests = new ArrayList<>();
    ArrayList<Integer> extendedMediaRequests = new ArrayList<>();


    // убрать storyData
    // убрать null


    public ChatMessagesRefactored(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
    }

    private void loadStoriesForMessages(long dialogId, ArrayList<MessageObject> visibleObjects) {
        if (visibleObjects.isEmpty()) {
            return;
        }
        StoryHandlerFactory storyHandlerFactory = new StoryHandlerFactory();
//        StringBuilder storyData = new StringBuilder();

//        for (int i = 0; i < visibleObjects.size(); i++) {
//            MessageObject messageObject = visibleObjects.get(i);
//            StoryHandler handler = storyHandlerFactory.getHandler(messageObject.type);
//
//            // Handle the story based on type
//            handler.handleStory(messageObject, storyData);
//
//            // Send request with simplified logic
//            sendStoryRequest(storyData.toString(), messageObject);
//        }
        visibleObjects.stream()
                .forEach(messageObject -> {
                    StoryHandler handler = storyHandlerFactory.getHandler(messageObject.type);

                    // Handle the story based on type
                    StringBuilder storyData = new StringBuilder(); // Initialize StringBuilder inside the lambda
                    handler.handleStory(messageObject, storyData);

                    // Send request with simplified logic
                    sendStoryRequest(storyData.toString(), messageObject);
                });

        cleanupOldRequests();
    }

    private void sendStoryRequest(String storyData, MessageObject messageObject) {
        TL_stories.TL_stories_getStoriesByID req = new TL_stories.TL_stories_getStoriesByID();
        req.peer = chatActivity.getMessagesController().getInputPeer(messageObject.messageOwner.media.user_id);
        req.id.add(Integer.parseInt(storyData));

        int reqId = chatActivity.getConnectionsManager().sendRequest(req, (response, error) -> {
            TL_stories.StoryItem newStoryItem = (response != null) ? ((TL_stories.TL_stories_stories) response).stories.get(0) : new TL_stories.TL_storyItemDeleted();
            updateUIWithStory(newStoryItem, messageObject);
        });
        extendedMediaRequests.add(reqId);
    }

    private void cleanupOldRequests() {
        if (extendedMediaRequests.size() > 10) {
            chatActivity.getConnectionsManager().cancelRequest(extendedMediaRequests.remove(0), false);
        }
    }

    private void updateUIWithStory(TL_stories.StoryItem storyItem, MessageObject messageObject) {
        boolean wasExpired = messageObject.isExpiredStory();
        StoriesStorage.applyStory(chatActivity.getCurrentAccount(), storyItem.dialogId, messageObject, storyItem);
        ArrayList<MessageObject> messageObjects = new ArrayList<>();
        messageObjects.add(messageObject);
        chatActivity.updateMessages(messageObjects, wasExpired && messageObject.type == MessageObject.TYPE_STORY_MENTION);
    }
}
