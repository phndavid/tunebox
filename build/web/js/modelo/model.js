var myApp = angular.module('myApp',['ngRoute','ngResource']);
var list = new Array();
var nameListActual = "";
var songlists = new Array();
myApp.config(function($routeProvider)
{
    $routeProvider.when('/',
        {
            templateUrl: 'view/list.html'
        });
});
//---------------------------------------------------------------------------------------------------------------
// Factorys
//---------------------------------------------------------------------------------------------------------------
myApp.factory("Playlist", function ($resource){
    return $resource("json/play.json",{}, { get: { method: "GET", isArray: true }});
});
myApp.controller('ControllerSongList', function($scope){
    $scope.upSongList = function() {
        $.get('./ServletSongList', function(responseJson) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
            list = responseJson;
            $scope.songlists = responseJson;
            console.log(list);
      });
    };
    $scope.sayNamelist = function(nameList){
          $scope.name = nameList;
          nameListActual = $scope.name;
          alert(nameListActual);
    };
    $scope.loadTotalSongList = function(){
      $.get('./ServletTotalSongList', function(responseJson) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
        $scope.songlists = responseJson;
      });
    };
    $scope.saveSonglist = function(nameSong,idSong){
        var data = nameSong+","+idSong; 
        $.post('./ServeltSaveSongList',data); 
    };
    $scope.isEven = function(){
        $scope.value = !$scope.value;
        console.log($scope.value);
        return $scope.value;
    };
});
myApp.controller('ControllerPlaylist', function($scope, $http, Playlist)
{
    $http.get('http://localhost:8080/TuneBox%20-%20javascript/json/play.json').
        success(function (data){
            $scope.playlists = data;

            $(document).ready(function(){
                var cssSelector ={jPlayer: "#jplayer_N",cssSelectorAncestor: "#jp_container_N"};
                var playlist = data;
                var options = {
                    playlistOptions: {
                        enableRemoveControls: true,
                        autoPlay: false
                    },
                    swfPath: "js/jPlayer",
                    supplied: "webmv, ogv, m4v, oga, mp3",
                    smoothPlayBar: true,
                    keyEnabled: true,
                    audioFullScreen: false
                };
                var myPlaylist = new jPlayerPlaylist(cssSelector,playlist,options);
                $(document).on($.jPlayer.event.pause, myPlaylist.cssSelector.jPlayer,  function(){
                    $('.musicbar').removeClass('animate');
                    $('.jp-play-me').removeClass('active');
                    $('.jp-play-me').parent('li').removeClass('active');
                });

                $(document).on($.jPlayer.event.play, myPlaylist.cssSelector.jPlayer,  function(){
                    $('.musicbar').addClass('animate');
                });

                $(document).on('click', '.jp-play-me', function(e){
                    e && e.preventDefault();
                    var $this = $(e.target);
                    if (!$this.is('a')) $this = $this.closest('a');

                    $('.jp-play-me').not($this).removeClass('active');
                    $('.jp-play-me').parent('li').not($this.parent('li')).removeClass('active');

                    $this.toggleClass('active');
                    $this.parent('li').toggleClass('active');
                    if( !$this.hasClass('active') ){
                        myPlaylist.pause();
                    }else{
                         $scope.searchAudio = function(audio){
                            console.log("Audio: "+audio);
                            var index = audio-1;
                            myPlaylist.play(index);
                        };
                        $scope.searchAudioTotallyList = function(audio){
                            console.log("Audio: "+audio);
                            var index = audio.id - 1;
                            myPlaylist.play(index);
                        };
                    }

                });
            });
        });
    $scope.Playlist = Playlist.get();
    $scope.addSonglist = function(audio){
        console.log("Nombre lista actual: "+nameListActual);
        for(i = 0; i < list.length; i++){
            console.log(list[i].canciones);
            if(list[i].nombre == nameListActual){
                list[i].canciones.push(audio);
                console.log(list);
            }
        }
    };
});
function SongLists(nameList, generoList, rutaImg){
    this.nameList = nameList;
    this.generoList = generoList;
    this.rutaImg = rutaImg;
    this.songs = [];
    this.sayNameList = function(){
        alert(this.nameList);
    };
}
function createSongList(){
    var namelist = $('#nameList').val();
    var generoList = $('#generoList').val();
    var rutaImg = $('#rutaImg').val();
    list.push(new SongLists(namelist,generoList,rutaImg));
    var info = namelist+","+generoList+","+rutaImg;
    alert(info);
    $.ajax({
           type: 'POST',
           url:  './ServletList',
           data: info,
           success: function (data) {
                   alert("Se agrego correctamente la lista con: "+namelist);
               }
           });
}


