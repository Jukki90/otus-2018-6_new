package department;

import atm.api.ATM;
import commands.Command;
import commands.GetBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Department {
    private static Logger logger = LoggerFactory.getLogger(Department.class);
    private List<ATM> atms;
    private final Map<String, Command> commands = new HashMap<>();
    private final String GET_BALANCE = "getBalance";
    private EventProducer producer = new EventProducer();


    public Department(List<ATM> atms) {
        this.atms = atms;
        commands.put(GET_BALANCE, new GetBalance());
        atms.forEach((item) -> {
            producer.addListener(item);
        });

    }


    public long getSummaryBalance() {
        return atms.stream().mapToLong(item -> commands.get(GET_BALANCE).execute(item)).sum();
    }

    public void restoreState() {
        atms.forEach((item) -> {
            producer.event();
        });
    }

    public void removeATM(ATM atm) {
        this.atms.forEach((item) -> {
            if (item.equals(atm)) {
                producer.removeListener(item);
                atms.remove(item);
            }
        });
    }

    public void addATM(ATM atm) {
        atms.add(atm);
        producer.addListener(atms.get(atms.size() - 1));
    }

    public List<ATM> getAtms() {
        return atms;
    }

}
