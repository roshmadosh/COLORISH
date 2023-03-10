import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AboutComponent} from "./about/about.component";
import {ColorsComponent} from "./colors/colors.component";

const routes: Routes = [
	{ path: 'home', component: ColorsComponent },
	{ path: 'about', component: AboutComponent },
]
@NgModule({
	imports: [
		RouterModule.forRoot(routes)
	],
	exports: [RouterModule]
})
export class AppRoutingModule {}
