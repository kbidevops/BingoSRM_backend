/*
@Author: jun jiyoun(TimBernersLee)
@email: greatroh@gmail.com
VERSION: 0.5.0
 [TABLE OF CONTENT]
 1) browser title(접근성 문서제목)
 2) sidebarEvent js 레프트메뉴
 3) item active
 4) sticky aside
 5) fileAttach js 파일첨부
 6) daterangepicker
 7) content width
 8) mCustom scrollbar
 */

$(function(){

    /*-------------------------------------------------------------------------------
        1) browser title js
    -------------------------------------------------------------------------------*/
    if($(document).find('.page-header').length !== 0){
        var title = $(".page-header h3").text();
        document.title = title + " < ITSM";
    };

    /*-------------------------------------------------------------------------------
        2) sidebarEvent js 레프트메뉴
    -------------------------------------------------------------------------------*/
    function sidebarEvent(){
        $('.lnb-menu>li:has(ul)').addClass("has-sub");
        $('.lnb-menu>li>a').on("click", function() {

            var checkElement = $(this).next();

            if($(".lnb-menu").hasClass('toggle')){
                $('.lnb-menu>li').removeClass('active');
                $('.lnb-menu ul').slideUp('normal');
                $(this).closest('li').addClass('active');
                if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
                    checkElement.slideUp('normal');
                }
                if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
                    $('.lnb-menu.toggle ul:visible').slideUp('normal');
                    checkElement.slideDown('normal');
                    $(this).closest('li').addClass('active');
                } else {
                    return true;
                }
            }
        });

        /* 메뉴 모두 펼치기 */
        $('.switch').val(this.checked);

        $(".switch").change(function(){
            if(this.checked){
                $('.lnb-menu').removeClass("toggle");
                $('.lnb-menu ul').slideDown('normal');
                $('.lnb-menu li').removeClass('active');
            } else {
                $('.lnb-menu').addClass("toggle");
                $('.lnb-menu ul').slideUp('normal');
                $(".current").addClass('active').parent("ul").slideDown();
                
            }
        }); 

        $(".lnb").mCustomScrollbar({
            theme:"dark", //테마
            mouseWheelPixels : 50, // 마우스휠 속도
            scrollInertia : 400 // 부드러운 스크롤 효과 적용
        });
  
    }
    sidebarEvent();

    /*-------------------------------------------------------------------------------
        3) item active js
    -------------------------------------------------------------------------------*/    

    function itemActive(){
        $(".cardlist.type2 .item").on("click",function(e){
            e.preventDefault();
            $(".cardlist.type2 .item").removeClass("active");
            $(this).addClass("active");

        });
    }
    if($(".cardlist.type2").length > 0 ){
        itemActive();
    }; 

    /*-------------------------------------------------------------------------------
        4) sticky aside js
    -------------------------------------------------------------------------------*/
    function stickyAside(){
        var $aside = $(".stickyaside"), 
            $window = $(window),
            offset  = $aside.offset();

        $window.scroll(function() {
            if ($window.scrollTop() > offset.top) {
                $aside.stop().animate({
                    marginTop: $window.scrollTop() - offset.top + 100
                });
            } else {
                $aside.stop().animate({
                    marginTop: 0
                });
            }
        });
    }
    if($(".stickyaside").length > 0 ){
        stickyAside();
    };

    /*-------------------------------------------------------------------------------
    $('.dFile').on('click', function(){
        var $fName = $(this).find('.file-name'),
            $inputFile = $(this).find('.input-file'),
            $fDelete = $(this).find('.file-delete'),
            $fSize = $(this).find('.file-size');

        $inputFile.on('change',function(e){

            fileName = this.files[0].name;
            iSize = this.files[0].size / 1024;

            if(iSize/1024 > 1){
                if(((iSize/1024)/1024)>1){
                    iSize = (Math.round(((iSize/1024)/1024)*100)/100);
                    fileSize = iSize + "GB"
                } else {
                    iSize = (Math.round((iSize/1024)*100)/100);
                    fileSize = iSize + "MB"
                }
            } else {
                iSize = (Math.round(iSize*100)/100);
                fileSize = iSize + "KB"
            }
            $fName.text(fileName);
            $fDelete.show();
            
            $fSize.text(fileSize).show();
            $fName.addClass("off");
        });

        $fDelete.on('click', function(){
            $inputFile.val("");
            $fName.text("");
            $fDelete.hide();
            $fSize.hide();
            $fName.removeClass("off");
        })
    });
    */

    /*-------------------------------------------------------------------------------
        6) daterangepicker
    -------------------------------------------------------------------------------*/
    if($(".singleDate").length > 0){
        $(".singleDate").daterangepicker({
            singleDatePicker: true,
            format: 'YYYY-MM-DD',
        });
    }

    /*-------------------------------------------------------------------------------
        7) content width
    -------------------------------------------------------------------------------*/
    if($(".srarea").length > 0){
        if($(".srarea").find(".col-right").length == 0){
            $("#content").addClass("mini");
        } 
    }

    /*-------------------------------------------------------------------------------
        8) scrollbox scrollbar
    -------------------------------------------------------------------------------*/
    if($(".scrollbox").length > 0){
        $(".scrollbox").mCustomScrollbar({
            theme:"dark", //테마
            mouseWheelPixels : 50, // 마우스휠 속도
            scrollInertia : 400 // 부드러운 스크롤 효과 적용
        });
    }
    
    /*-------------------------------------------------------------------------------
    	9) a tag preventDefault
	-------------------------------------------------------------------------------*/
    
    $('a[href="#"]').click(function(e) {
    	e.preventDefault();
    });

});

