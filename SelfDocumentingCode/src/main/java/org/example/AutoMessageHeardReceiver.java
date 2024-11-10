package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.telegram.tgnet.TLRPC;

/*
Класс - часть функционала экранов с переписками в Android-приложении Телеграма.
Его роль - получив соответствующий broadcast, пометить все сообщения и все реакты на стороне этого пользователя в этом чате
вплоть до определённого ("maxId") включительно как "прочитанные".
Сама работа по изменению статуса выполняется вызовом god-object класса MessagesController из этого же модуля.
 */

public class AutoMessageHeardReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationLoader.postInitApplication();
        long dialogId = intent.getLongExtra("dialog_id", 0);
        int maxId = intent.getIntExtra("max_id", 0);
        int currentAccount = intent.getIntExtra("currentAccount", 0);
        if (dialogId == 0 || maxId == 0 || !UserConfig.isValidAccount(currentAccount)) {
            return;
        }
        AccountInstance accountInstance = AccountInstance.getInstance(currentAccount);
        if (DialogObject.isUserDialog(dialogId)) {
            TLRPC.User user = accountInstance.getMessagesController().getUser(dialogId);
            if (user == null) {
                Utilities.globalQueue.postRunnable(() -> {
                    TLRPC.User user1 = accountInstance.getMessagesStorage().getUserSync(dialogId);
                    AndroidUtilities.runOnUIThread(() -> {
                        accountInstance.getMessagesController().putUser(user1, true);
                        MessagesController.getInstance(currentAccount).markDialogAsRead(dialogId, maxId, maxId, 0, false, 0, 0, true, 0);
                        MessagesController.getInstance(currentAccount).markReactionsAsRead(dialogId, 0);
                    });
                });
                return;
            }
        } else if (DialogObject.isChatDialog(dialogId)) {
            TLRPC.Chat chat = accountInstance.getMessagesController().getChat(-dialogId);
            if (chat == null) {
                Utilities.globalQueue.postRunnable(() -> {
                    TLRPC.Chat chat1 = accountInstance.getMessagesStorage().getChatSync(-dialogId);
                    AndroidUtilities.runOnUIThread(() -> {
                        accountInstance.getMessagesController().putChat(chat1, true);
                        MessagesController.getInstance(currentAccount).markDialogAsRead(dialogId, maxId, maxId, 0, false, 0, 0, true, 0);
                        MessagesController.getInstance(currentAccount).markReactionsAsRead(dialogId, 0);
                    });
                });
                return;
            }
        }
        MessagesController.getInstance(currentAccount).markDialogAsRead(dialogId, maxId, maxId, 0, false, 0, 0, true, 0);
        MessagesController.getInstance(currentAccount).markReactionsAsRead(dialogId, 0);
    }
}