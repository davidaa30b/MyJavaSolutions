package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Outlook implements MailClient {

    private final static int FIRST_OF_PATH = 1;
    private final static int BEG_OF_PRIORITY = 1;
    private final static int END_OF_PRIORITY = 10;


    private Map<String, String> emailAndAccountName; // email , accountName
    private Map<String, Account> accountsMap; //account Name ,account

    private Map<String, List<String>> accountsWithFiles;

    private Map<String, List<Rule>> accountsWithRules;

    private Map<String, List<String>> accountsWithExistingFolders;

    private List<String> existingBeginningsOfPaths ;

    private Map<String, Map<String, List<Mail>>> accountsWithMailInFolders; //accountName,folderPath,mail;
    private boolean checkStringNotValid(String str) {
        return str == null || str.isBlank() || str.isEmpty();
    }

    private boolean checkPriorityNotValid(int priority) {
        return priority < BEG_OF_PRIORITY || priority > END_OF_PRIORITY;
    }

    private boolean checkRuleNotValid(String ruleDescription) {
        String[] arrOfRuleDefinition = ruleDescription.split(System.lineSeparator());
        int counterSubjectIncludes = 0;
        int counterSubjectOrBodyIncludes = 0;
        int counterRecipientsIncludes = 0;
        int counterFrom = 0;

        for (int i = 0; i < arrOfRuleDefinition.length; i++) {
            System.out.println("(" + i + "). " + arrOfRuleDefinition[i]);
            if (arrOfRuleDefinition[i].contains("subject-includes")) {
                counterSubjectIncludes++;
            }

            if (arrOfRuleDefinition[i].contains("subject-or-body-includes")) {
                counterSubjectOrBodyIncludes++;
            }

            if (arrOfRuleDefinition[i].contains("recipients-includes")) {
                counterRecipientsIncludes++;
            }

            if (arrOfRuleDefinition[i].contains("from")) {
                counterFrom++;
            }
        }

        return counterSubjectIncludes > 1 || counterSubjectOrBodyIncludes > 1 ||
             counterRecipientsIncludes > 1 || counterFrom > 1;

    }


    public Outlook() {
        accountsMap = new HashMap<>();
        accountsWithFiles = new HashMap<>();
        existingBeginningsOfPaths = new ArrayList<>();

        existingBeginningsOfPaths.add("inbox");
        existingBeginningsOfPaths.add("sent");

        accountsWithRules = new HashMap<>();
        accountsWithExistingFolders = new HashMap<>();
        accountsWithMailInFolders = new HashMap<>();
        emailAndAccountName = new HashMap<>();

    }

    public Map<String, List<Rule>> getRulesAccount() {
        return accountsWithRules;
    }

    public Map<String, Map<String, List<Mail>>> getAccountsWithMailInFolders() {
        return accountsWithMailInFolders;
    }

    @Override
    public Account addNewAccount(String accountName, String email) {

        if (checkStringNotValid(accountName) || checkStringNotValid(email)) {
            throw new IllegalArgumentException("Error ! Invalid account name or email !");
        }

        if (accountsMap.containsKey(accountName)) {
            throw new AccountAlreadyExistsException("Error ! Account already exists ! ");
        }

        accountsMap.put(accountName, new Account(email, accountName));
        accountsWithFiles.put(accountName, new ArrayList<>());
        accountsWithRules.put(accountName, new ArrayList<>());
        accountsWithExistingFolders.put(accountName, new ArrayList<>());
        accountsWithMailInFolders.put(accountName, new HashMap<>());
        accountsWithMailInFolders.get(accountName).putIfAbsent("/inbox", new ArrayList<>());
        accountsWithMailInFolders.get(accountName).putIfAbsent("/sent", new ArrayList<>());
        emailAndAccountName.put(email, accountName);
        return new Account(email, accountName);
    }

    @Override
    public void createFolder(String accountName, String path) {
        if (checkStringNotValid(accountName) || checkStringNotValid(path)) {
            throw new IllegalArgumentException("Error ! Invalid account name or path !");
        }

        if (!accountsMap.containsKey(accountName)) {
            throw new AccountNotFoundException("Error ! Account with this account name does not exist ! ");
        }

        String[] arrOfPath = path.split("/");

        if (!existingBeginningsOfPaths.contains(arrOfPath[FIRST_OF_PATH])) {
            throw new InvalidPathException("Error ! Invalid path ! ");
        }

        if (arrOfPath.length > 2 ) {
            for (int i = 2; i < arrOfPath.length - 1; i++) {
                if (!accountsWithExistingFolders.get(accountName).contains(arrOfPath[i])) {
                    throw new InvalidPathException("Error ! Invalid path ! ");
                }
            }
        }


        if (accountsWithFiles.get(accountName).contains(path)) {
            throw new FolderAlreadyExistsException("Error ! This path already exists for this account ! ");
        }

        accountsWithFiles.get(accountName).add(path);
        accountsWithExistingFolders.get(accountName).add(arrOfPath[arrOfPath.length - 1]);
    }

    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {
        if (checkStringNotValid(accountName) || checkStringNotValid(folderPath)
             || checkStringNotValid(ruleDefinition) || checkPriorityNotValid(priority)) {
            throw new IllegalArgumentException("Error ! Invalid account name, folder path, " +
              "rule definition or priority ");
        }

        if ( !accountsWithFiles.containsKey(accountName)) {
            throw new AccountNotFoundException("Error ! Invalid account, it does not exists ! ");
        }

        if (!accountsWithFiles.get(accountName).contains(folderPath))  {
            throw new FolderNotFoundException("Error ! Folder path, it does not exists ! ");
        }

        if (checkRuleNotValid(ruleDefinition)) {
            throw new RuleAlreadyDefinedException("Error ! Can not have duplicating conditions in the rule !");
        }

        accountsWithRules.get(accountName).add(new Rule(ruleDefinition, folderPath, priority));

    }

    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {

        if (checkStringNotValid(accountName) || checkStringNotValid(mailMetadata) ||
            checkStringNotValid(mailContent)) {
            throw new IllegalArgumentException("Error ! Invalid account name, mail meta data or mail content");
        }

        if (!accountsMap.containsKey(accountName)) {
            throw new AccountNotFoundException("Error ! Invalid account name, it does not exist");
        }

        String[] mailMetadataArr = mailMetadata.split(System.lineSeparator());

        String mailSender = "";
        List<String> mailSubjectKeys = new ArrayList<>();
        List<String> mailRecipients = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();

        List<Rule> matchedRules = new ArrayList<>();

        for (int m = 0; m < mailMetadataArr.length; m++) {

            if (mailMetadataArr[m].contains("sender")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("sender:", "");
                mailSender = mailMetadataArr[m].trim();
            }

            if (mailMetadataArr[m].contains("subject")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("subject:", "");
                String[] elements = mailMetadataArr[m].split(",");
                for (String element : elements) {
                    mailSubjectKeys.add(element.trim());
                }
            }

            if (mailMetadataArr[m].contains("recipients")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("recipients:", "");
                String[] elements = mailMetadataArr[m].split(",");
                for (String element : elements) {
                    mailRecipients.add(element.trim());
                }
            }

            if (mailMetadataArr[m].contains("received")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("received:", "");
                dateTime = LocalDateTime.parse(mailMetadataArr[m].trim(), formatter);
            }
        }

        Mail mail = new Mail(accountsMap.get(mailSender), new HashSet<>(mailRecipients),
                mailSubjectKeys.toString(), mailContent, dateTime);

        for (int i = 0; i < accountsWithRules.get(accountName).size(); i++) {
            String[] ruleArr = accountsWithRules.get(accountName).get(i).ruleDefinition().split(System.lineSeparator());

            int ruleApproves = 0;

            for (String s : ruleArr) {

                if (s.contains("subject-includes")) {
                    int e;
                    for (e = 0; e < mailSubjectKeys.size(); e++) {
                        if (s.contains(mailSubjectKeys.get(e))) {
                            s.replace(mailSubjectKeys.get(e), "");
                        }
                    }
                    if (s.isBlank()) {
                        ruleApproves++;
                    }
                }

                if (s.contains("subject-or-body-includes")) {
                    String[] elements = s.split(",");
                    int e;
                    for (e = 0; e < elements.length; e++) {
                        if (!mailContent.contains(elements[e])) {
                            break;
                        }
                    }

                    if (e == elements.length - 1) {
                        ruleApproves++;
                    }
                }

                if (s.contains("recipients")) {
                    for (String mailRecipient : mailRecipients) {
                        if (s.contains(mailRecipient)) {
                            ruleApproves++;
                            break;
                        }
                    }
                }

                if (s.contains("from")) {
                    if (s.contains(mailSender)) {
                        ruleApproves++;
                    }
                }
            }

            if (ruleApproves >= ruleArr.length - 1) {
                matchedRules.add(accountsWithRules.get(accountName).get(i));
            }
        }

        int maxIndex = 0;
        int maxValue = 0;

        if (matchedRules.size() == 0) {
            accountsWithMailInFolders.get(accountName).get("/inbox").add(mail);
        }
        else {
            for (int i = 0; i < matchedRules.size(); i++) {
                if (matchedRules.get(i).priority() > maxValue) {
                    maxValue = matchedRules.get(i).priority();
                    maxIndex = i;
                }
            }
            accountsWithMailInFolders.get(accountName).putIfAbsent(matchedRules.get(maxIndex).folderPath(),
                    new ArrayList<>());
            accountsWithMailInFolders.get(accountName).get(matchedRules.get(maxIndex).folderPath()).add(mail);

        }

    }

    @Override
    public Collection<Mail> getMailsFromFolder(String account, String folderPath) {
        if (checkStringNotValid(account) || checkStringNotValid(folderPath)) {
            throw new IllegalArgumentException("Error ! Account name or folder path is invalid  ! ");
        }

        if (!accountsMap.containsKey(account)) {
            throw new AccountNotFoundException("Error ! Account does not exist ! ");
        }

        if (!accountsWithFiles.get(account).contains(folderPath)) {
            throw new FolderNotFoundException("Error ! Folder does not exist ! ");
        }


        return accountsWithMailInFolders.get(account).get(folderPath);
    }

    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {
        if (checkStringNotValid(accountName) || checkStringNotValid(mailMetadata)
             || checkStringNotValid(mailContent)) {
            throw new IllegalArgumentException("Error ! Invalid account name," +
              " mail metadata or mail content ! ");
        }

        String[] mailMetadataArr = mailMetadata.split(System.lineSeparator());

        String mailSender = "";
        List<String> mailSubjectKeys = new ArrayList<>();
        List<String> mailRecipients = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();

        for (int m = 0; m < mailMetadataArr.length; m++) {

            if (mailMetadataArr[m].contains("sender")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("sender:", "");
                mailSender = mailMetadataArr[m].trim();
            }

            if (mailMetadataArr[m].contains("subject")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("subject:", "");
                String[] elements = mailMetadataArr[m].split(",");
                for (String element : elements) {
                    mailSubjectKeys.add(element.trim());
                }
            }
            if (mailMetadataArr[m].contains("recipients")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("recipients:", "");
                String[] elements = mailMetadataArr[m].split(",");
                for (String element : elements) {
                    mailRecipients.add(element.trim());
                }
            }

            if (mailMetadataArr[m].contains("received")) {
                mailMetadataArr[m] = mailMetadataArr[m].replace("received:", "");
                dateTime = LocalDateTime.parse(mailMetadataArr[m].trim(), formatter);
            }
        }

        Mail mail = new Mail(accountsMap.get(mailSender),
                new HashSet<>(mailRecipients), mailSubjectKeys.toString(), mailContent, dateTime);
        accountsWithMailInFolders.get(accountName).get("/sent").add(mail);

        for (String mailRecipient : mailRecipients) {
            if (emailAndAccountName.containsKey(mailRecipient)) {
                receiveMail(emailAndAccountName.get(mailRecipient), mailMetadata, mailContent);
            }
        }

    }
}

//8mi dekemvri beshe tejyk : )
