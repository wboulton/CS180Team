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
	SEARCH, ADD_FRIEND, REMOVE_FRIEND, BLOCK, CREATE_USER, VERIFY_LOGIN, CHANGE_USERNAME,

	// message
	UNBLOCK, GET_SENT_MESSAGES, GET_RECIEVED_MESSAGES, RECOVER_MESSAGES,
	GET_USER, SEND_MESSAGE, DELETE_MESSAGE, EDIT_MESSAGE
}
