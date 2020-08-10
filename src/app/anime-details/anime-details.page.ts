import { Component, OnInit } from "@angular/core";
import { AnimeShared } from "../shared/anime-details";
import { ModalController } from "@ionic/angular";
import { VideoplayerPage } from "../modals/videoplayer/videoplayer.page";
import { HttpClient } from "@angular/common/http";
@Component({
  selector: "app-anime-details",
  templateUrl: "./anime-details.page.html",
  styleUrls: ["./anime-details.page.scss"],
})
export class AnimeDetailsPage implements OnInit {
  titulli;
  img;
  pershkrimi;
  episodesa;
  streamUrl = `https://salty-anchorage-64305.herokuapp.com/api/v1/AnimeEpisodeHandler/`;
  constructor(
    private modalController: ModalController,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.titulli = AnimeShared.title;
    this.img = AnimeShared.img;
    this.pershkrimi = AnimeShared.description;
    this.episodesa = AnimeShared.episodes;
  }

  async onClick(a) {
    const modal = await this.modalController.create({
      component: VideoplayerPage,
      componentProps: {
        iframe: this.streamUrl + a,
      },
    });
    return await modal.present();
  }
}
