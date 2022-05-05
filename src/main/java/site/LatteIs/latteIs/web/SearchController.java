package site.LatteIs.latteIs.web;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.PhoneAuthenticationService;
import site.LatteIs.latteIs.auth.SessionUser;
import site.LatteIs.latteIs.web.domain.entity.Blacklist;
import site.LatteIs.latteIs.web.domain.entity.Follower;
import site.LatteIs.latteIs.web.domain.entity.Interest;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.BlacklistRepository;
import site.LatteIs.latteIs.web.domain.repository.FollowerRepository;
import site.LatteIs.latteIs.web.domain.repository.InterestRepository;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;

    @GetMapping("/search")
    public String search(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            int init = userRepository.findByUsername(user.getUsername()).getInit();
            if(init == 0) // 관심사 없음
                model.addAttribute("moreInfo", 0);
        }
        return "search";
    }

    @GetMapping("/mbtiFriend")
    public String mbtiFriend(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            User userinfo = userRepository.findByUsername(user.getUsername());
            Interest userInterest = interestRepository.findByUserId(userinfo.getId());

            //String userMBTI = userInterest.getMbti();
            String userMBTI = "ENTJ";
            System.out.println("사용자의 MBTI : " + userMBTI);
            String good;
            if(userMBTI.equals("ENFJ") || userMBTI.equals("INTJ")){
                if(userMBTI.equals("ENFJ"))
                    good = "INTJ";
                else
                    good = "ENFJ";
            }
            else if(userMBTI.equals("ENTJ") || userMBTI.equals("INFJ")){
                if(userMBTI.equals("ENTJ"))
                    good = "INFJ";
                else
                    good = "ENTJ";
            }
            else if(userMBTI.equals("ESTJ") || userMBTI.equals("ISFJ")){
                if(userMBTI.equals("ESTJ"))
                    good = "ISFJ";
                else
                    good = "ESTJ";
            }
            else if(userMBTI.equals("ESFJ") || userMBTI.equals("ISTJ")){
                if(userMBTI.equals("ESFJ"))
                    good = "ISTJ";
                else
                    good = "ESFJ";
            }
            else if(userMBTI.equals("ENFP") || userMBTI.equals("ISFP")){
                if(userMBTI.equals("ENFP"))
                    good = "ISFP";
                else
                    good = "ENFP";
            }
            else if(userMBTI.equals("ENTP") || userMBTI.equals("ISTP")){
                if(userMBTI.equals("ENTP"))
                    good = "ISTP";
                else
                    good = "ENTP";
            }
            else if(userMBTI.equals("ESTP") || userMBTI.equals("INTP")){
                if(userMBTI.equals("ESTP"))
                    good = "INTP";
                else
                    good = "ESTP";
            }
            else if(userMBTI.equals("ESFP") || userMBTI.equals("INFP")){
                if(userMBTI.equals("ESFP"))
                    good = "INFP";
                else
                    good = "ESFP";
            }
            else
                good = null;
            System.out.println("good : " + good);
            good = "ENFP"; //테스트용
            model.addAttribute("searchMbti", good);

            List<Interest> userList = interestRepository.findAllByMbtiandUniversity(good, userInterest.getUniversity(), userInterest.getUser().getId());
            model.addAttribute("userList", userList);

            System.out.println("userList : " + userList);
        }
        return "mbtiFriend";
    }

    @GetMapping("/hobbyFriend")
    public String hobbyFriend(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            User userinfo = userRepository.findByUsername(user.getUsername());
            Interest userInterest = interestRepository.findByUserId(userinfo.getId());

            String hobby = userInterest.getHobby();
            System.out.println(hobby);
            String[] arr = {"", "", ""};
            String[] regdx = {",", "\\[", "\\]"};
            if(hobby.contains(",")){
                System.out.println(hobby.split(",").length + "개");
                int start = 0, end = 0;
                for(int i = 0; i < hobby.split(",").length; i++){
                    end = hobby.indexOf(",", start);
                    if(end == -1)
                        end = hobby.length();
                    arr[i] = hobby.substring(start, end);
                    for(int j = 0; j < regdx.length; j++)
                        arr[i] = arr[i].replaceAll(regdx[j], "");
                    start = end + 1;
                    System.out.println("arr["+ i + "]: " + arr[i]);
                }
            }
            else{
                System.out.println("1개");
                for(int j = 0; j < regdx.length; j++)
                    arr[0] = arr[0].replaceAll(regdx[j], "");
                for(int i = 0; i < arr.length; i ++)
                    System.out.println(arr[i]);
            }

            List<Interest> userList = interestRepository.findAllByEqualInterest(arr[0], arr[1], arr[2], userInterest.getUniversity(), userInterest.getUser().getId());
            System.out.println("userList : " + userList);

            model.addAttribute("userList", userList);

        }
        return "hobbyFriend";
    }

    @GetMapping("/friendDetail")
    public String friendDetail(@RequestParam(value = "user_id") Long user_id, Model model, @LoginUser SessionUser user, Interest friendInterest){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            User userinfo = userRepository.findByUsername(user.getUsername());

            System.out.println("받은 user_id : " + user_id);
            model.addAttribute("user_id", user_id);
            friendInterest = interestRepository.findByUserId(user_id.intValue());
            model.addAttribute("friendInterest", friendInterest);

            Follower follower = followerRepository.findByUserId(userinfo.getId());
            Blacklist blacklist = blacklistRepository.findByUserId(userinfo.getId());
            System.out.println("follower : " + follower);
            System.out.println("blacklist : " + blacklist);

            if(follower.getFollowerUserIdList().contains(Long.toString(user_id)))
                model.addAttribute("alreadyFollower", 1);
            else if(blacklist.getBlackUserIdList().contains(Long.toString(user_id)))
                model.addAttribute("alreadyBlacklist", 1);


        }
        return "friendDetail";
    }

    @ResponseBody
    @GetMapping("/friendDetail/follower")
    public void follower(@RequestParam ("user_id") String user_id, @LoginUser SessionUser user, Follower follower) {
        User userinfo = userRepository.findByUsername(user.getUsername());
        System.out.println("로그인 유저 : " + userinfo);
        User followerUser = userRepository.findById(Integer.parseInt(user_id));
        System.out.println(followerUser.getNickName() + "님 팔로우 신청");

        follower.setUser(userinfo);
        System.out.println("follwerList : " + follower.getFollowerUserIdList());
        if(follower.getFollowerUserIdList() == null)
            follower.setFollowerUserIdList(user_id);
        else
            follower.setFollowerUserIdList(follower.getFollowerUserIdList() + "," + user_id);
        followerRepository.save(follower);
        System.out.println("follwer 정보 : " + follower);

    }

    @ResponseBody
    @GetMapping("/friendDetail/black")
    public void black(@RequestParam ("user_id") String user_id, @LoginUser SessionUser user, Blacklist blacklist) {
        User userinfo = userRepository.findByUsername(user.getUsername());
        System.out.println("로그인 유저 : " + userinfo);
        User blacklistUser = userRepository.findById(Integer.parseInt(user_id));
        System.out.println(blacklistUser.getNickName() + "님 팔로우 신청");
        blacklist.setUser(blacklistUser);

        System.out.println("follwerList : " + blacklist.getBlackUserIdList());
        if(blacklist.getBlackUserIdList() == null)
            blacklist.setBlackUserIdList(user_id);
        else
            blacklist.setBlackUserIdList(blacklist.getBlackUserIdList() + "," + user_id);
        blacklistRepository.save(blacklist);
        System.out.println("blacklist 정보 : " + blacklist);
    }
}
