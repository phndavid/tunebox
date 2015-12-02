var myApp = angular.module('myApp',['ngRoute','ngResource']);
var list = new Array();
var nameListActual = "";
//---------------------------------------------------------------------------------------------------------------
// Config
//---------------------------------------------------------------------------------------------------------------
myApp.config(function($routeProvider)
{
    $routeProvider.when('/new',
        {
            templateUrl: 'view/createlist.html'
        })
        .when('/playlist',
        {
            templateUrl: 'view/playlist.html'
        })
        .when('/listaCanciones',
        { 
            templateUrl:'view/listaCanciones.jsp'
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
        console.log("Lista de canciones: "+list);
        $scope.songlists = list;
        console.log("Lista de canciones: "+list);

    };
    $scope.sayNamelist = function(nameList){
          $scope.name = nameList;
          nameListActual = $scope.name;
          alert(nameListActual);
    };
});
myApp.controller('ControllerPlaylist', function($scope, $http, Playlist)
{
    $http.get('http://localhost:8080/TuneBox/json/play.json').
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
                        $scope.searchAudio = function(index){
                            console.log(index);
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
            console.log(list[i].songs);
            if(list[i].nameList == nameListActual){
                list[i].songs.push(audio);
                console.log(list);
            }
        }
       /*
        $http({
            method: 'POST',
            url: 'http://localhost:63342/jplayer/principal.html',
            data: angular.toJson(play),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })*/
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
    alert(list);
}