import { Component, OnInit } from "@angular/core";
import { DomSanitizer, SafeResourceUrl } from "@angular/platform-browser";
import { HttpClient } from "@angular/common/http";
@Component({
  selector: "app-videoplayer",
  templateUrl: "./videoplayer.page.html",
  styleUrls: ["./videoplayer.page.scss"],
})
export class VideoplayerPage implements OnInit {
  iframe;
  animeDetail;
  finalUrl;

  constructor(public sanitizer: DomSanitizer, private http: HttpClient) {}

  ngOnInit() {
    return this.http.get(this.iframe).subscribe((data) => {
      this.animeDetail = data["anime"];
      for (let i = 0; i < this.animeDetail.length; i++) {
        const element = this.animeDetail[i]["servers"][2]["iframe"];
        this.finalUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
          element + "?autoplay=1"
        );
      }
    });
  }
}
