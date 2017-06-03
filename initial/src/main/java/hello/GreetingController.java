package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.math.BigInteger;
import java.security.SecureRandom;

@SessionAttributes({"urlId","username"})
@Controller
public class GreetingController {

    @RequestMapping("/planyourtrip")
    public String planyourtrip(@RequestParam(value="username", required=true) String username,
                               @RequestParam(value="url", required=false) String url, Model model) {

        System.out.println("Username " + username);
        System.out.println("Url " + url);
        String urlId;
        if (StringUtils.isEmpty(username)) {
            return "redirect:/index.html";
        }
        else if (StringUtils.isEmpty(url)) {
            urlId = generateUrlId();
            //addUrlIdUsernameToDB(urlId,username);
            model.addAttribute("urlId", urlId);
            model.addAttribute("username", username);
        } else {
            try {
                validateUrlAndUsername(url,username);
                model.addAttribute("urlId", url.substring(url.length() - 10));
                model.addAttribute("username", username);
            } catch (Exception ex) {
                model.addAttribute("exceptionMessage", "Incorrect URL!");
            }
        }
        return "planyourtrip";
    }

    private void validateUrlAndUsername(String url, String username) throws Exception{
        String urlId;
        boolean isValidUrlId = false;
        if (url.length() >= 10) {
            urlId = url.substring(url.length() - 10);
            if (isUrlIdPresentInDB(urlId)) {
                isValidUrlId = true;
                addUserNameIfNotPresentInDB(username);
            }
        }
        if (!isValidUrlId) {
            throw new Exception();
        }
    }

    private boolean isUrlIdPresentInDB(String urlId) {
        return true;
        //check if URL_ID exists in UserDetails Table. If yes return true
    }

    private void addUserNameIfNotPresentInDB(String username) {
        //check if USERNAME exists in UserDetails Table. If not add to Table
    }

    private String generateUrlId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(60, random).toString(32).substring(0,10);
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}