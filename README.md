# Tipping-Calculator

An Android app coded in Java. After a pleasant meal the app can calculate a tip based on the bill. 
Users can define percentage of what they consider a generous, pleasant, and average tip. The 
app will then determine the tip amount based on user’s dining experience and calculate the total 
expense.

Java
	- DOH.java 
		- Database helper class
		- Creates the SQLite databases to keep the application stateful
			- When user exit outs of app, the tip and percentages are still kept
		- TIP_TABLE keeps track of user's tipping history
		- RATE_TABLE saves the current tip and tax rate set by the user

	- MainActivity.java (layout: activity_main.xml)
		- The main page of the tipping calculator app
		- User inputs their bill and select their satisfaction level
		- App will calculate the bill's tip, tax, and total
		- The update button lets user update tip and/or tag percentage
			- The application will run a explicit intent to Update.java
		- The history button will show user their bill history
		- The confirm button will add the current bill, tip, tax, and total to the TIP_TABLE

	- Update.java (layout: update_activity.xml)
		- This page is where user can update the tip and tax percentages
		- Each percentage by default will be the old percentages in the event user doesn't want to proceed with updating
		- The new percentages will replace the old percentages in RATE_TABLE

	- History.java (layout: view_activity.xml)
		- This page shows user their tipping history, this is the financial tracker for the application
		- The history is kept in TIP_TABLE
		- Each row of the TIP_TABLE is added to ListView, which acts similar to an ArrayList with the most recent transaction at the beginning
			- Each element in the array is displayed using (layout: line_view.xml)
		- User can view each bill in more detail by clicking on the individual transactions
		- User can also delete a transaction from the list by pressing and holding on the item they want to remove
			- Removal will completely delete that specific transaction from the database as well

	- TransactionDetail.java (transaction_detail.xml)
		- Will display the selected bill from History.java in more detail

<p>
<img src="https://user-images.githubusercontent.com/61510855/137009996-74b5f3c7-619d-4eb8-9e4b-252e2c532020.png" width="250" height="500">

<img src="https://user-images.githubusercontent.com/61510855/137010013-cd741054-ae02-4fc6-970b-874be52a1e3a.png" width="250" height="500">

<img src="https://user-images.githubusercontent.com/61510855/137010023-12eac0c7-fb9a-4974-8944-ddac57adc925.png" width="250" height="500">
</p>
