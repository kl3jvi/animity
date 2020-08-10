import { Component } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { AnimeShared } from "../shared/anime-details";
@Component({
  selector: "app-tab1",
  templateUrl: "tab1.page.html",
  styleUrls: ["tab1.page.scss"],
})
export class Tab1Page {
  animeUrl = "https://salty-anchorage-64305.herokuapp.com/api/v1/Search/";
  searchUrl = this.animeUrl + "/Search/";
  data;
  searchset;
  searched = false;
  popularAnimes;
  episodes;
  toggled = true;
  genres;
  constructor(private http: HttpClient, private router: Router) {}
  slideOpts = {
    slidesPerView: 3,
  };

  onClick(titulli, pershkrimi, imazhi, episodet) {
    this.router.navigateByUrl("/anime-details");
    AnimeShared.title = titulli;
    AnimeShared.description = pershkrimi;
    AnimeShared.img = imazhi;
    AnimeShared.episodes = episodet;
    console.log(episodet);
  }

  ngOnInit() {
    var nu = Math.floor(Math.random() * 10);
    console.log(nu.toString());
    var ju = nu.toString();

    return this.http
      .get("https://salty-anchorage-64305.herokuapp.com/api/v1/Popular/" + ju)
      .subscribe((data) => {
        this.popularAnimes = data["popular"];
      });
  }

  filterAnime(evt) {
    var q = evt.target.value;
    console.log(q);
    if (q.trim() == "") {
      this.searched = false;
    } else {
      this.searched = true;
      this.http.get(this.animeUrl + q.trim()).subscribe((data) => {
        this.searchset = data["search"];
        for (let i = 0; i < this.searchset.length; i++) {
          this.genres = this.searchset[i]["genres"];
        }
      });
    }
  }
}
