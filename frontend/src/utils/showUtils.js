export function getShowHorizontalPosterUrl(show) {
  if (!show || !show.imageSet || !show.imageSet.horizontalPoster) return null
  const imageSet = show.imageSet.horizontalPoster
  
  if (imageSet.w720) {
    return imageSet.w720
  } else if (imageSet.w480) {
    return imageSet.w480
  } else if (imageSet.w360) {
    return imageSet.w360
  } else if (imageSet.w240) {
    return imageSet.w240
  }

  return null
}

export function getShowVerticalPosterUrl(show) {
  if (!show || !show.imageSet || !show.imageSet.verticalPoster) return null
  const imageSet = show.imageSet.verticalPoster
  
  if (imageSet.w720) {
    return imageSet.w720
  } else if (imageSet.w480) {
    return imageSet.w480
  } else if (imageSet.w360) {
    return imageSet.w360
  } else if (imageSet.w240) {
    return imageSet.w240
  }

  return null
}

export function getShowYears(show) {
  if (!show) return ''
  
  if (show.releaseYear) {
    return `${show.releaseYear}`
  } else if (show.firstAirYear && show.lastAirYear) {
    return `${show.firstAirYear} - ${show.lastAirYear}`
  }

  return `unknown`
}