'use strict';

/* Controllers */

var phonecatControllers = angular.module('phonecatControllers', []);

phonecatControllers.controller('PhoneListCtrl', ['$scope', 'Phone',
  function($scope, Phone) {
    $scope.phones = Phone.query();
    $scope.orderProp = 'age';
  }]);

phonecatControllers.controller('PhoneDetailCtrl', ['$scope', '$routeParams', 'Phone',
  function($scope, $routeParams, Phone) {
    $scope.phone = Phone.get({phoneId: $routeParams.phoneId}, function(phone) {
      $scope.mainImageUrl = phone.images[0];
    });

    $scope.setImage = function(imageUrl) {
      $scope.mainImageUrl = imageUrl;
    }
  }]);


var loginControllers = angular.module('loginControllers', []);

loginControllers.controller('LoginListCtrl', ['$scope','$http',
  function($scope,$http) {
    $scope.username='703350556@qq.com';
    $scope.password='abcde';
    $scope.subLogin =function(){
        $http({
          method:'post',
          url:'http://localhost:8082/home/index/login.do',
          data:{"email":$scope.username,"password":$scope.password,"mac":'abc'},
          headers:{'Content-Type': 'application/x-www-form-urlencoded'},  
          transformRequest: function(obj) {  
             var str = [];  
             for(var p in obj){  
               str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));  
             }  
             return str.join("&");  
           }
          
        }).success(function(data,status,headers,config){
          alert(data.success);
        }).error(function(data,status,headers,config){
          console.log(data);
        });
    };
  }]);
