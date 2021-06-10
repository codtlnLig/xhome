function addValidatorMobilePhone()
{
	jQuery.validator.addMethod("mobilePhone", function(value, element) {
			var length = value.length;
			var tel = /^((1[0-9]{2})+\d{8})$/;
			return this.optional(element) || (length == 11 && tel.test(value));
		}, "手机号码错误 ");
}
function addValidatorAllPhone()
{
	//校验手机固话(放宽), 原校验规则(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\(\d{3}\))|(\d{3}\-))?(1[358]\d{9})$)
	 jQuery.validator.addMethod("allPhone", function(value, element) {
		    var allphone= /(^(0[0-9]{2,3}\-)?([1-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d{3}\-)|([0-9]{3}))?(1[2345689]\d{9})$)/;
			return this.optional(element) || (allphone.test(value));
		}, "手机或固话");
}

function addValidatorFloat()
{
jQuery.validator.addMethod("FloatCheck", function(value, element) {
			return this.optional(element) || /^(0|[1-9][0-9]{0,5})(\.\d{1,2})?$/.test(value);
		}, "格式：整数位最多6位,小数位最多2");
}
function addValidatorPersonID(){
	//支持新老身份证
	jQuery.validator.addMethod("PRCPersonId", function(value, element) {
			return this.optional(element) || /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/.test(value);
		}, "身份证格式不正确");
}

function addValidatorPhoneFax(){
	//校验普通固话传真;
	jQuery.validator.addMethod("PhoneFax", function(value, element) {
			return this.optional(element) ||/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/.test(value);}, "格式不正确");
}
function addValidatorEnStrNum(){
	//字母数字;
	jQuery.validator.addMethod("EnStrNum", function(value, element) {
	 return this.optional(element) ||/^[a-zA-Z0-9]+$/.test(value);}, "格式不正确");
}

function addValidatorNumString(){
	//数字组成String;
	jQuery.validator.addMethod("NumString", function(value, element) {
	 return this.optional(element) ||/^[0-9]*$/.test(value);}, "格式不正确");
}

function addValidatorInteger(){
	//数字组成String;
	jQuery.validator.addMethod("integer", function(value, element) {
	 return this.optional(element) ||/^[1-9][0-9]*$/.test(value);}, "格式不正确");
}

