<form name="form1" method="post" action="">
    <label>
		Palindrome. Input string:
		<input type="text" name="textfield" id="textfield">
	</label>
    <label>
		<input type="submit" name="button" id="button" value="Find">
	</label>
</form>
<?php 
if(isset($_POST['button']))
	process($_POST['textfield']);
 
#FINDING THE LONGEST PALINDROME
#AT THE GIVEN STRING
function process($str){
	print "<p>Inputed: ";
	echo $str;
	print "</p>";
	print "<p>Palindrome is ";
	$str = str_replace(" ","",$str);
    $palindrome = $str[0];
    $i = 1;
    $L = 0;
    $R = 2;
    while($R<strlen($str)){
        if($L>-1 && strcasecmp($str[$L],$str[$R])==0){
            if(strlen($palindrome)<strlen(substr($str,$L,$R-$L+1)))
                $palindrome = substr($str,$L,$R-$L+1);
            $L--; #moving to the begin of word
            $R++; #moving to the end of word
        
        }
        else if(strcasecmp($str[$i],$str[$R])==0)
            $L = $i;
        else if(strcasecmp($str[$i],$str[$L])==0)
            $R = $i;
        else{
            $i = $R;
            $L = $i - 1;
            $R++;
        }
        
    }
    echo $palindrome;
	print "</p>";
}
?>
