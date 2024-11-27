//import java.util.ArrayList;
//
//import org.telegram.tgnet.TLRPC;
//import org.telegram.tgnet.tl.TL_stories;
//import org.telegram.ui.ChatActivity;
//import org.telegram.ui.Stories.StoriesStorage;
//
//public class ChatMessages {
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
//    public ChatMessagesMetadataController(ChatActivity chatActivity) {
//        this.chatActivity = chatActivity;
//    }
//
//    private void loadStoriesForMessages(long dialogId, ArrayList<MessageObject> visibleObjects) {
//        if (visibleObjects.isEmpty()) {
//            return;
//        }
//        for (int i = 0; i < visibleObjects.size(); i++) {
//
//            TL_stories.TL_stories_getStoriesByID req = new TL_stories.TL_stories_getStoriesByID();
//
//            MessageObject messageObject = visibleObjects.get(i);
//            TL_stories.StoryItem storyItem = new TL_stories.TL_storyItem();
//            if (messageObject.type == MessageObject.TYPE_STORY || messageObject.type == MessageObject.TYPE_STORY_MENTION) {
//                storyItem = messageObject.messageOwner.media.storyItem;
//                storyItem.dialogId = messageObject.messageOwner.media.user_id;
//            } else if (messageObject.messageOwner.reply_to != null) {
//                storyItem = messageObject.messageOwner.replyStory;
//                storyItem.dialogId = DialogObject.getPeerDialogId(messageObject.messageOwner.reply_to.peer);
//            } else {
//                continue;
//            }
//            long storyDialogId = storyItem.dialogId;
//            req.peer = chatActivity.getMessagesController().getInputPeer(storyDialogId);
//            req.id.add(storyItem.id);
//            int storyId = storyItem.id;
//            int reqId = chatActivity.getConnectionsManager().sendRequest(req, (response, error) -> {
//                TL_stories.StoryItem newStoryItem = null;
//                if (response != null) {
//                    TL_stories.TL_stories_stories stories = (TL_stories.TL_stories_stories) response;
//                    if (stories.stories.size() > 0) {
//                        newStoryItem = stories.stories.get(0);
//                    }
//                    if (newStoryItem == null) {
//                        newStoryItem = new TL_stories.TL_storyItemDeleted();
//                    }
//                    newStoryItem.lastUpdateTime = System.currentTimeMillis();
//                    newStoryItem.id = storyId;
//                    TL_stories.StoryItem finalNewStoryItem = newStoryItem;
//                    AndroidUtilities.runOnUIThread(() -> {
//                        boolean wasExpired = messageObject.isExpiredStory();
//                        StoriesStorage.applyStory(chatActivity.getCurrentAccount(), storyDialogId, messageObject, finalNewStoryItem);
//                        ArrayList<MessageObject> messageObjects = new ArrayList<>();
//                        messageObject.forceUpdate = true;
//                        messageObjects.add(messageObject);
//                        chatActivity.getMessagesStorage().getStorageQueue().postRunnable(() -> {
//                            chatActivity.getMessagesController().getStoriesController().getStoriesStorage().updateMessagesWithStories(messageObjects);
//                        });
//                        if (!wasExpired && messageObject.isExpiredStory() && messageObject.type == MessageObject.TYPE_STORY_MENTION) {
//                            chatActivity.updateMessages(messageObjects, true);
//                        } else {
//                            chatActivity.updateMessages(messageObjects, false);
//                        }
//                    });
//                }
//            });
//            extendedMediaRequests.add(reqId);
//        }
//        if (extendedMediaRequests.size() > 10) {
//            chatActivity.getConnectionsManager().cancelRequest(extendedMediaRequests.remove(0), false);
//        }
//    }
//}
