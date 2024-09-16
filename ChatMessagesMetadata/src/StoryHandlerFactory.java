import java.util.HashMap;
import java.util.Map;

public class StoryHandlerFactory {
    private Map<Integer, StoryHandler> handlers;

    public StoryHandlerFactory() {
        handlers = new HashMap<>();
        // Load handlers for different types of stories
        handlers.put(MessageObject.TYPE_STORY, new RegularStoryHandler());
        handlers.put(MessageObject.TYPE_STORY_MENTION, new MentionStoryHandler());
        handlers.put(ReplyStoryHandler.TYPE_REPLY, new ReplyStoryHandler());
        // ... add other handlers as needed
    }

    public StoryHandler getHandler(int type) {
        return handlers.getOrDefault(type, new DefaultStoryHandler());
    }
}

public class RegularStoryHandler implements StoryHandler {
    @Override
    public void handleStory(MessageObject messageObject, StringBuilder storyData) {
        TL_stories.StoryItem storyItem = messageObject.messageOwner.media.storyItem;
        storyItem.dialogId = messageObject.messageOwner.media.user_id;
        storyData.append(storyItem.id);
    }
}

public class ReplyStoryHandler implements StoryHandler {
    public static final int TYPE_REPLY = 2;

    @Override
    public void handleStory(MessageObject messageObject, StringBuilder storyData) {
        TL_stories.StoryItem storyItem = messageObject.messageOwner.replyStory;
        storyItem.dialogId = DialogObject.getPeerDialogId(messageObject.messageOwner.reply_to.peer);
        storyData.append(storyItem.id).append(" Reply");
    }
}
//
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
//public class MentionStoryHandler implements StoryHandler {
//    @Override
//    public void handleStory(MessageObject messageObject, StringBuilder storyData) {
//        TL_stories.StoryItem storyItem = messageObject.messageOwner.media.storyItem;
//        storyItem.dialogId = messageObject.messageOwner.media.user_id;
//        storyData.append(storyItem.id).append(" Mention");
//    }
//}
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
//public class DefaultStoryHandler implements StoryHandler {
//    @Override
//    public void handleStory(MessageObject messageObject, StringBuilder storyData) {
//        //some basic behavior
//    }
//}