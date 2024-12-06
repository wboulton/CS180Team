/**
 * Team Project -- Action
 *
 * This file contains the Actions for the Threads management
 * For more in depth documentation see Docs/threading.md
 *
 * @author Alan Yi
 *
 * @version November 1, 2024
 *
 */
public enum Action {
	// user
	SEARCH, ADD_FRIEND, REMOVE_FRIEND, BLOCK, CREATE_USER, VERIFY_LOGIN,
		CHANGE_USERNAME, CHANGE_PASSWORD, LOGIN, UNBLOCK, GET_USER,CHANGE_PICTURE, GET_FRIEND, 
		ALLOW_ALL, GET_PROFILEPICTURE, SET_PROFILEPICTURE,

	// message
	GET_CONVERSATION, RECOVER_MESSAGES, SEND_MESSAGE, DELETE_MESSAGE, EDIT_MESSAGE, SET_VIEWING, GET_SENT_MESSAGES,
	GET_RECEIVED_MESSAGES
}
