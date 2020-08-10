import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AnimeDetailsPage } from './anime-details.page';

const routes: Routes = [
  {
    path: '',
    component: AnimeDetailsPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AnimeDetailsPageRoutingModule {}
