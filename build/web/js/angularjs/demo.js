var myApp = angular.module('myApp',['ngRoute','ngResource']);
var name = "default";
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
            templateUrl: 'view/playlist.jsp'
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
//---------------------------------------------------------------------------------------------------------------
// Controllers
//---------------------------------------------------------------------------------------------------------------
// <!-- Controlador vistas  de playlist -->
myApp.controller('ControllerPlaylist', function($scope, $http, Playlist){
    //http://localhost:8080/TuneBox/
    //http://tunebox.jelastic.servint.net/
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
    $scope.sayName = function() {
        $scope.name_list = $scope.nameList;
        var nombreLista =  $scope.name_list;
        name = nombreLista;
    };
    $scope.addSongPlaylist = function(index){
        console.log(index);
        var info = index+","+name;
        // The actual from POST method
        $.ajax({
            type: 'POST',
            url:  './ServletList',
            data: info,
            success: function (data) {
                    console.log("Hey, we got reply form java side, with following data: ");
                    console.log("Data:"+data);
                    alert("Se agrego correctamente la cancion con id: "+index+" a la lista: "+name);
                    // redirecting example..
                    if(data === "SUCCESS") {
                        window.location.replace("http://stackoverflow.com");
                    }

                }
            });
    };
});