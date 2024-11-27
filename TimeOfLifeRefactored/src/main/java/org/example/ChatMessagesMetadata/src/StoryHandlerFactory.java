//import java.util.HashMap;
//import java.util.Map;
//
//
//public class StoryHandlerFactory {
//    private Map<Integer, StoryHandler> handlers;
//
//    public StoryHandlerFactory() {
//        handlers = new HashMap<>();
//        handlers.put(MessageObject.TYPE_STORY, new RegularStoryHandler());
//        handlers.put(MessageObject.TYPE_STORY_MENTION, new RegularStoryHandler());
//        handlers.put(ReplyStoryHandler.TYPE_REPLY, new ReplyStoryHandler());
//    }
//
//    public StoryHandler getHandler(int type) {
//        return handlers.getOrDefault(type, new DefaultStoryHandler());
//    }
//}
//
//public interface StoryHandler {
//    void handleStory(MessageObject messageObject, StringBuilder storyData);
//}
//
//public class RegularStoryHandler implements StoryHandler {
//    @Override
//    public void handleStory(MessageObject messageObject, StringBuilder storyData) {
//        TL_stories.StoryItem storyItem = messageObject.messageOwner.media.storyItem;
//        storyItem.dialogId = messageObject.messageOwner.media.user_id;
//        storyData.append(storyItem.id);
//    }
//}
//
//
//public class ReplyStoryHandler implements StoryHandler {
//    public static final int TYPE_REPLY = 2;
//
//    @Override
//    public void handleStory(MessageObject messageObject, StringBuilder storyData) {
//        TL_stories.StoryItem storyItem = messageObject.messageOwner.replyStory;
//        storyItem.dialogId = DialogObject.getPeerDialogId(messageObject.messageOwner.reply_to.peer);
//        storyData.append(storyItem.id).append(" Reply");
//    }
//}
//
//class DefaultStoryHandler implements StoryHandler {
//    @Override
//    public void handleStory(MessageObject messageObject, StringBuilder storyData) {
//        return;
//    }
//}
