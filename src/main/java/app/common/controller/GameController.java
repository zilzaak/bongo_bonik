package app.common.controller;


import app.common.dto.MsgResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fifa")
@CrossOrigin("*")
public class GameController {
    @GetMapping("/round/first")
    ResponseEntity<Score> firstRound(@RequestParam Map<String,String> mp) throws RuntimeException{
        String team = mp.get("team");
        Score score = new Score();
        if(team.equalsIgnoreCase("Argentina")){
            score.setScore(5);
        }
        else if(team.equalsIgnoreCase("Brazil")){
            score.setScore(2);
        }
        else if(team.equalsIgnoreCase("Uruguye")){
            score.setScore(6);
        }
        else if(team.equalsIgnoreCase("Germany")){
            score.setScore(4);
        }else{
            score.setScore(0);
        }
        return new ResponseEntity<Score>(score , HttpStatus.OK);
    }

    @GetMapping("/round/second")
    ResponseEntity<Score> secondRound(@RequestParam Map<String,String> mp) throws RuntimeException{
        Integer firstRoundScore = Integer.parseInt(mp.get("firstScore"));
        Score score = new Score();
        if(firstRoundScore>0 && firstRoundScore<4){
            score.setScore(15);
        }
        else if(firstRoundScore>3 && firstRoundScore<7){
            score.setScore(10);
        }
        else if(firstRoundScore>6 && firstRoundScore<10){
            score.setScore(18);
        }

        else if(firstRoundScore>9 && firstRoundScore<20){
            score.setScore(20);
        }
        else{
            score.setScore(0);
        }
        return new ResponseEntity<Score>(score , HttpStatus.OK);
    }



    @GetMapping("/round/isEligible")
    ResponseEntity<Eligible> isEligible(@RequestParam Map<String,String> mp) throws RuntimeException{
        Integer firstScore = Integer.parseInt(mp.get("firstScore"));
        Integer secondScore = Integer.parseInt(mp.get("secondScore"));
        var sum = firstScore+secondScore;
        Eligible response = new Eligible();
        response.setTotal(sum);
        if(sum>15){
            response.setEligible("yes");

        }else{
            response.setEligible("no");
        }
        return new ResponseEntity<Eligible>(response , HttpStatus.OK);
    }



}
