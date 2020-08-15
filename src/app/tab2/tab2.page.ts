import { Component } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { DomSanitizer } from "@angular/platform-browser";
import { AnimeShared } from "../shared/anime-details";
import { Router } from "@angular/router";

@Component({
  selector: "app-tab2",
  templateUrl: "tab2.page.html",
  styleUrls: ["tab2.page.scss"],
})
export class Tab2Page {
  movies;
  vidLink;
  searchset;
  animeUrl = "https://salty-anchorage-64305.herokuapp.com/api/v1/Search/";
  genres;
  searched = false;
  temp;
  constructor(
    private http: HttpClient,
    private router: Router,
    public sanitizer: DomSanitizer
  ) {}
  ngOnInit() {
    this.getRecentSeries();
    this.vidLink = this.sanitizer.bypassSecurityTrustResourceUrl(
      "https://cloud9.to/embed/ew-bvPn7rYXu"
    );
  }

  homelogo() {
    this.searched = false;
  }

  getRecentSeries() {
    var page_number = Math.floor(Math.random() * 69);
    return this.http
      .get(
        "https://salty-anchorage-64305.herokuapp.com/api/v1/Movies/" +
          page_number.toString()
      )
      .subscribe((data) => {
        this.movies = data["movies"];
        console.log(this.movies);
      });
  }

  onClick1(episodes) {
    var episodeId;
    for (let i = 0; i < episodes.length; i++) {
      episodeId = episodes[i].id;

      this.getIframeSrc(episodeId);
    }
  }

  getIframeSrc(episodeId) {
    var iframeSrc;
    return this.http
      .get(
        "https://salty-anchorage-64305.herokuapp.com/api/v1/AnimeEpisodeHandler/" +
          episodeId
      )
      .subscribe((data) => {
        for (let i = 0; i < data["anime"].length; i++) {
          const element = data["anime"][i]["servers"][2]["iframe"];
          console.log(element);
          this.vidLink = this.sanitizer.bypassSecurityTrustResourceUrl(
            element + "?autoplay=1"
          );
        }
      });
  }
}
