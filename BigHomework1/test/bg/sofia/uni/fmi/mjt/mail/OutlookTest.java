package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OutlookTest {

    @InjectMocks
    private Outlook client;

    @Test
    void testAddNewAccountNullAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.addNewAccount(null,"something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testAddNewAccountBlankAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.addNewAccount(" ","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testAddNewAccountEmptyAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.addNewAccount("","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testAddNewAccountNullEmail(){
        assertThrows(IllegalArgumentException.class, ()-> client.addNewAccount("something",null) ,
                "Error ! Account email is invalid !");
    }

    @Test
    void testAddNewAccountEmptyEmail(){
        assertThrows(IllegalArgumentException.class, ()-> client.addNewAccount("something","") ,
                "Error ! Account email is invalid !");
    }

    @Test
    void testAddNewAccountBlankEmail(){
        assertThrows(IllegalArgumentException.class, ()-> client.addNewAccount("something"," ") ,
                "Error ! Account email is invalid !");
    }

    @Test
    void testAddNewAccountExistingAccount() throws AccountAlreadyExistsException{

            client.addNewAccount("david","davidbaruch@abv.bg");


        assertThrows(AccountAlreadyExistsException.class, ()-> client.addNewAccount("david","othermail@abv.bg") ,
                "Error ! Account already exists ! ");
    }

    @Test
    void testAddNewAccountReturnAccount() throws AccountAlreadyExistsException{
        assertEquals(client.addNewAccount("david","davidbaruchreal@gmail.com"),
                new Account("davidbaruchreal@gmail.com","david"));
    }

    @Test
    void testCreateFolderNullAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.createFolder(null,"something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testCreateFolderEmptyAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.createFolder("","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testCreateFolderBlankAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.createFolder(" ","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testCreateFolderNullPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.createFolder("something",null) ,
                "Error ! Path is invalid !");
    }

    @Test
    void testCreateFolderEmptyPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.createFolder("something","") ,
                "Error ! Path is invalid !");
    }

    @Test
    void testCreateFolderBlankAPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.createFolder("something"," ") ,
                "Error ! Path is invalid ! ");
    }

    @Test
    void testCreateFolderUnexcitingAccount() {

        assertThrows(AccountNotFoundException.class, ()-> client.createFolder("somethingName","somethingPath") ,
                "Error ! Account with this account name does not exists ! ");
    }

    @Test
    void testCreateFolderNonexistentBegin() throws AccountAlreadyExistsException {
        client.addNewAccount("somethingName","somethingEmail");

        assertThrows(InvalidPathException.class, ()-> client.createFolder("somethingName","/somethingPath/again/nota-gain") ,
                "Error ! The file path starts from non-existing folder !");
    }

    @Test
    void testCreateFolderNonexistentIntermediateFolders() throws AccountAlreadyExistsException, FolderAlreadyExistsException, AccountNotFoundException, InvalidPathException {
        client.addNewAccount("somethingName","somethingEmail");
        client.createFolder("somethingName","/inbox/folder1");
        assertThrows(InvalidPathException.class, ()-> client.createFolder("somethingName","/inbox/folder2/folder3") ,
                "Error ! The file path starts from non-existing folder !");
    }


    @Test
    void testCreateFolderExistingPath() throws AccountAlreadyExistsException, FolderAlreadyExistsException, AccountNotFoundException, InvalidPathException {
        client.addNewAccount("somethingName","somethingEmail");
        client.createFolder("somethingName","/inbox/again");
        client.createFolder("somethingName","/inbox/again/nota-gain");

        assertThrows(FolderAlreadyExistsException.class, ()-> client.createFolder("somethingName","/inbox/again/nota-gain") ,
                "Error ! The file path already exists for this account ! ");
    }
    @Test
    void testAddRuleNullAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule(null,"something","something",10) ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testAddRuleBlankAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule(" ","something","something",10) ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testAddRuleEmptyAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("","something","something",10) ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testAddRuleNullFolderPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something",null,"something",10) ,
                "Error ! Folder path is invalid !");
    }

    @Test
    void testAddRuleBlankFolderPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something"," ","something",10) ,
                "Error ! Folder path is invalid !");
    }

    @Test
    void testAddRuleEmptyFolderPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something","","something",10) ,
                "Error ! Folder path is invalid !");
    }
    //
    @Test
    void testAddRuleNullRuleDefinition(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something","something",null,10) ,
                "Error ! Rule definition is invalid !");
    }

    @Test
    void testAddRuleBlankRuleDefinition(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something","something"," ",10) ,
                "Error ! Rule definition is invalid !");
    }

    @Test
    void testAddRuleEmptyRuleDefinition(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something","something","",10) ,
                "Error ! Rule definition is invalid !");
    }

    @Test
    void testAddRuleOutOfRangeLeftPriority(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something","something","something",0) ,
                "Error ! Priority is invalid ! ");
    }

    @Test
    void testAddRuleOutOfRangeRightPriority(){
        assertThrows(IllegalArgumentException.class, ()-> client.addRule("something","something","something",12) ,
                "Error ! Priority is invalid ! ");
    }

    @Test
    void testAddRuleNonExistingAccount(){
        assertThrows(AccountNotFoundException.class, ()-> client.addRule("nonExisting","nonExisting","nonExisting",9) ,
                "Error ! Account does not exists ! ");
    }

    @Test
    void testAddRuleNonExistingFolderPath() throws AccountAlreadyExistsException {
        client.addNewAccount("account","email");
        assertThrows(FolderNotFoundException.class, ()-> client.addRule("account","nonExisting","something",9) ,
                "Error ! Folder path does not exists ! ");
    }

    @Test
    void testAddRuleAlreadyExistingCondition() throws AccountAlreadyExistsException, FolderAlreadyExistsException, AccountNotFoundException, InvalidPathException {
        client.addNewAccount("account","email");
        client.createFolder("account","/inbox/something");
        String ruleDefinition = "subject-includes: <list-of-keywords>" +System.lineSeparator()+
                "subject-or-body-includes: <list-of-keywords>" +System.lineSeparator()+
                "recipients-includes: <list-of-recipient-emails>" +System.lineSeparator()+
                "recipients-includes: blah blah blah" +System.lineSeparator()+
                "from: <sender-email>";
        assertThrows(RuleAlreadyDefinedException.class,()->client.addRule("account",
                "/inbox/something",ruleDefinition,9));
    }

    @Test
    void testAddRuleCorrectEntry() throws AccountAlreadyExistsException, FolderAlreadyExistsException, AccountNotFoundException, InvalidPathException, FolderNotFoundException, RuleAlreadyDefinedException {
        client.addNewAccount("account","email");
        client.createFolder("account","/inbox/something");
        String ruleDefinition = "subject-includes: <list-of-keywords>" +System.lineSeparator()+
                "subject-or-body-includes: <list-of-keywords>" +System.lineSeparator()+
                "recipients-includes: <list-of-recipient-emails>" +System.lineSeparator()+
                "from: <sender-email>";

        Map<String, List<Rule>> expectedAccountsWithRules = new HashMap<>();
        List<Rule>rules=new ArrayList<>();
        rules.add(new Rule(ruleDefinition,"/inbox/something",9));
        expectedAccountsWithRules.put("account",rules);
        client.addRule("account", "/inbox/something",ruleDefinition,9);
        assertEquals(expectedAccountsWithRules,client.getRulesAccount());
    }

    @Test
    void testReceiveMailNullAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail(null,"something","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailBlankAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail(" ","something","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailEmptyAccountName(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail("","something","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailNullMailMetaData(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail("something",null,"something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailBlankMailMetaData(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail("something"," ","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailEmptyMailMetaData(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail("something","","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailNullMailContent(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail("something","something",null) ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailBlankMailContent(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail("something","something"," ") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailEmptyMailContent(){
        assertThrows(IllegalArgumentException.class, ()-> client.receiveMail("something","something","") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testReceiveMailNonExistingAccount(){
        assertThrows(AccountNotFoundException.class, ()-> client.receiveMail("nonExisting","something","something") ,
                "Error ! Account name is invalid , this account does not exists !");
    }

    @Test
    void testReceiveMailCorrect() throws AccountAlreadyExistsException, FolderAlreadyExistsException, AccountNotFoundException, InvalidPathException, FolderNotFoundException, RuleAlreadyDefinedException {
        client.addNewAccount("david","davidbaruchreal@gmail.com");
        client.addNewAccount("roberto","robertobaruch@gmail.com");
        client.createFolder("david","/inbox/folder1");
        client.createFolder("david","/inbox/folder2");

        String ruleDef1="subject-includes: hut,save" + System.lineSeparator()+
                "subject-or-body-includes: censored" + System.lineSeparator()+
                "recipients-includes: david" + System.lineSeparator()+
                "from: roberto";

        String ruleDef2="subject-includes: hut,save" + System.lineSeparator()+
                "subject-or-body-includes: public" + System.lineSeparator()+
                "recipients-includes: david" + System.lineSeparator()+
                "from: roberto";


        client.addRule("david","/inbox/folder1",ruleDef1,2);
        client.addRule("david","/inbox/folder2",ruleDef2,3);

        String metaData="sender: roberto" + System.lineSeparator()+
                "subject: hut,save" + System.lineSeparator()+
                "recipients: david" + System.lineSeparator()+
                "received: 2022-12-10 16:03";

        String mailContent="I hate public hotels";

        client.receiveMail("david",metaData,mailContent);

        //Map<String,Map<String,List<Mail>>> result=client.getAccountsWithMailInFolders();
       // for (var entry : result.entrySet()) {
        //    System.out.println("Key = " + entry.getKey() + "Value = "+ entry.getValue() );
       //}
        Map<String,Map<String,List<Mail>>> expectedAccountsWithMailsFolders = new HashMap<>() ;
        expectedAccountsWithMailsFolders.put("roberto",new HashMap<>());
        expectedAccountsWithMailsFolders.put("david",new HashMap<>());

        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/inbox", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/sent", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("roberto").putIfAbsent("/inbox", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("roberto").putIfAbsent("/sent", new ArrayList<>());

        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/inbox/folder2",new ArrayList<>());
        Set<String> expectedRecipients = new HashSet<>();
        expectedRecipients.add("david");
        List expectedSubjectKeys = new ArrayList();
        expectedSubjectKeys.add("hut");
        expectedSubjectKeys.add("save");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dateTime = LocalDateTime.parse("2022-12-10 16:03" ,formatter);
        expectedAccountsWithMailsFolders.get("david").get("/inbox/folder2").
                add(new Mail(new Account("robertobaruch@gmail.com","roberto"),
                        expectedRecipients,expectedSubjectKeys.toString(),mailContent,dateTime));


        assertEquals(expectedAccountsWithMailsFolders,client.getAccountsWithMailInFolders());

    }

    @Test
    void testGetMailsFromFolderNullAccount(){
        assertThrows(IllegalArgumentException.class, ()-> client.getMailsFromFolder(null,"something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testGetMailsFromFolderBlankAccount(){
        assertThrows(IllegalArgumentException.class, ()-> client.getMailsFromFolder(" ","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testGetMailsFromFolderEmptyAccount(){
        assertThrows(IllegalArgumentException.class,()-> client.getMailsFromFolder("","something")  ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testGetMailsFromFolderNullFolderPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.getMailsFromFolder("something",null) ,
                "Error ! Folder path is invalid !");
    }

    @Test
    void testGetMailsFromFolderBlankFolderPath(){
        assertThrows(IllegalArgumentException.class, ()-> client.getMailsFromFolder("something"," ") ,
                "Error ! Folder path is invalid !");
    }

    @Test
    void testGetMailsFromFolderEmptyFolderPath(){
        assertThrows(IllegalArgumentException.class,()-> client.getMailsFromFolder("something","")  ,
                "Error ! Folder path is invalid !");
    }

    @Test
    void testGetMailsFromFolderNonExistentAccount(){
        assertThrows(AccountNotFoundException.class,()-> client.getMailsFromFolder("NonSomething","something")  ,
                "Error ! The account does not exist !");
    }

    @Test
    void testGetMailsFromFolderNonExistentFolder() throws AccountAlreadyExistsException {
        client.addNewAccount("something","something");
        assertThrows(FolderNotFoundException.class,()-> client.getMailsFromFolder("something","inbox/shit")  ,
                "Error ! The account does not exist !");
    }

    @Test
    void testGetMailsFromFolderCorrect() throws AccountAlreadyExistsException, FolderNotFoundException, RuleAlreadyDefinedException, FolderAlreadyExistsException, AccountNotFoundException, InvalidPathException {
        client.addNewAccount("david","davidbaruchreal@gmail.com");
        client.addNewAccount("roberto","robertobaruch@gmail.com");
        client.createFolder("david","/inbox/folder1");
        client.createFolder("david","/inbox/folder2");

        String ruleDef1="subject-includes: hut,save" + System.lineSeparator()+
                "subject-or-body-includes: censored" + System.lineSeparator()+
                "recipients-includes: david" + System.lineSeparator()+
                "from: roberto";

        String ruleDef2="subject-includes: hut,save" + System.lineSeparator()+
                "subject-or-body-includes: public" + System.lineSeparator()+
                "recipients-includes: david" + System.lineSeparator()+
                "from: roberto";


        client.addRule("david","/inbox/folder1",ruleDef1,2);
        client.addRule("david","/inbox/folder2",ruleDef2,3);

        String metaData="sender: roberto" + System.lineSeparator()+
                "subject: hut,save" + System.lineSeparator()+
                "recipients: david" + System.lineSeparator()+
                "received: 2022-12-10 16:03";

        String mailContent="I hate public hotels";

        client.receiveMail("david",metaData,mailContent);

        Map<String,Map<String,List<Mail>>> expectedAccountsWithMailsFolders = new HashMap<>() ;
        expectedAccountsWithMailsFolders.put("roberto",new HashMap<>());
        expectedAccountsWithMailsFolders.put("david",new HashMap<>());

        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/inbox", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/sent", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("roberto").putIfAbsent("/inbox", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("roberto").putIfAbsent("/sent", new ArrayList<>());

        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/inbox/folder2",new ArrayList<>());
        Set<String> expectedRecipients = new HashSet<>();
        expectedRecipients.add("david");
        List expectedSubjectKeys = new ArrayList();
        expectedSubjectKeys.add("hut");
        expectedSubjectKeys.add("save");
        LocalDateTime dateTime ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dateTime = LocalDateTime.parse("2022-12-10 16:03" ,formatter);
        expectedAccountsWithMailsFolders.get("david").get("/inbox/folder2").
                add(new Mail(new Account("robertobaruch@gmail.com","roberto"),
                        expectedRecipients,expectedSubjectKeys.toString(),mailContent,dateTime));


        assertEquals(expectedAccountsWithMailsFolders.get("david").get("/inbox/folder2"),client.getMailsFromFolder("david","/inbox/folder2"));
    }


    @Test
    void testSendMailNullAccount(){
        assertThrows(IllegalArgumentException.class, ()-> client.sendMail(null,"something","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testSendMailBlankAccount(){
        assertThrows(IllegalArgumentException.class, ()-> client.sendMail(" ","something","something") ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testSendMailEmptyAccount(){
        assertThrows(IllegalArgumentException.class,()-> client.sendMail("","something","something")  ,
                "Error ! Account name is invalid !");
    }

    @Test
    void testSendMailNullMailMetadata(){
        assertThrows(IllegalArgumentException.class, ()-> client.sendMail("something",null,"something") ,
                "Error ! Mail Metadata is invalid !");
    }

    @Test
    void testSendMailBlankMailMetadata(){
        assertThrows(IllegalArgumentException.class, ()-> client.sendMail("something"," ","something") ,
                "Error ! Mail Metadata is invalid !");
    }

    @Test
    void testSendMailEmptyMailMetadata(){
        assertThrows(IllegalArgumentException.class,()-> client.sendMail("something","","something")  ,
                "Error ! Mail Metadata is invalid !");
    }

    @Test
    void testSendMailNullMailContent(){
        assertThrows(IllegalArgumentException.class, ()-> client.sendMail("something","something",null) ,
                "Error ! Mail Content is invalid !");
    }

    @Test
    void testSendMailBlankMailContent(){
        assertThrows(IllegalArgumentException.class, ()-> client.sendMail("something","something"," ") ,
                "Error ! Mail Content is invalid !");
    }

    @Test
    void testSendMailEmptyMailContent(){
        assertThrows(IllegalArgumentException.class,()-> client.sendMail("something","something","")  ,
                "Error ! Mail Content is invalid !");
    }

    @Test
    void testSendMailAddToSent() throws AccountNotFoundException, AccountAlreadyExistsException {
        client.addNewAccount("david","davidbaruchreal@gmail.com");
        client.addNewAccount("roberto","robertobaruch@gmail.com");

        String metaData="sender: roberto" + System.lineSeparator()+
                "subject: hut,save" + System.lineSeparator()+
                "recipients: david" + System.lineSeparator()+
                "received: 2022-12-10 16:03";

        String mailContent="I hate public hotels";

        client.sendMail("roberto",metaData,mailContent);

        Map<String,Map<String,List<Mail>>> expectedAccountsWithMailsFolders=new HashMap<>();
        expectedAccountsWithMailsFolders.put("roberto",new HashMap<>());
        expectedAccountsWithMailsFolders.put("david",new HashMap<>());

        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/inbox", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("david").putIfAbsent("/sent", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("roberto").putIfAbsent("/inbox", new ArrayList<>());
        expectedAccountsWithMailsFolders.get("roberto").putIfAbsent("/sent", new ArrayList<>());

        Map<String,Map<String,List<Mail>>> result = client.getAccountsWithMailInFolders();
        Set<String> expectedRecipients = new HashSet<>();
        expectedRecipients.add("david");
        List expectedSubjectKeys = new ArrayList();
        expectedSubjectKeys.add("hut");
        expectedSubjectKeys.add("save");
        LocalDateTime dateTime ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dateTime = LocalDateTime.parse("2022-12-10 16:03" ,formatter);

        expectedAccountsWithMailsFolders.get("roberto").get("/sent").
                add(new Mail(new Account("robertobaruch@gmail.com","roberto"),
                        expectedRecipients,expectedSubjectKeys.toString(),mailContent,dateTime ));
        assertEquals(expectedAccountsWithMailsFolders.get("roberto").get("/sent"),
                client.getAccountsWithMailInFolders().get("roberto").get("/sent"));
    }
}
