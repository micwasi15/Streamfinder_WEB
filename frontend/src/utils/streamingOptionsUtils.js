import api from '@/axios'

export function getAllCountries() {
    let res = api.get('/api/public/countries')
    return res.then(response => {
        if (response.status === 200) {
            return response.data
        } else {
            throw new Error('Failed to fetch countries')
        }
    }).catch(error => {
        console.error('Error fetching countries:', error)
        throw error
    })
}

export function getStreamingOptionsServices(streamingOptions) {
    if (!streamingOptions?.streamingOptions) return []

    const res = []
    const names = new Set()
    streamingOptions.streamingOptions.forEach(option => {
        if (option?.options) {
            option.options.forEach(service => {
                if (service?.service && !names.has(service.service.name)) {
                    res.push({
                        name: service.service.name,
                        logoURL: service.service.logoURL,
                    })
                    names.add(service.service.name)
                }
            })
        }
    })
    return res
}

export async function getStreamingOptionsCountries(streamingOptions) {
    if (!streamingOptions?.streamingOptions) return []

    const allCountries = await getAllCountries()
    if (!allCountries) return []

    const res = []
    streamingOptions.streamingOptions.forEach(option => {
        if (option?.country) {
            const existing = allCountries.find(c => c.code === option.country)
            res.push({
                code: option.country,
                name: existing?.name || option.country,
            })
        }
    })
    return res
}

export async function mapCountryCodesToNames(countries) {
    if (!countries || !Array.isArray(countries)) return []

    const allCountries = await getAllCountries()
    if (!allCountries) return []

    return countries.map(country => ({
        code: country,
        name: allCountries[country]?.name || country,
    }))
}

export function getAvailabilityByCountry(streamingOptions, services) {
    if (!streamingOptions?.streamingOptions || !services) return {}

    const availability = {}

    streamingOptions.streamingOptions.forEach(option => {
        if (option?.country && option?.options) {
            availability[option.country] = {}
            option.options.forEach(service => {
                if (service?.service?.name) {
                    availability[option.country][service.service.name] = true
                }
            })
            services.forEach(service => {
                if (!availability[option.country][service.name]) {
                    availability[option.country][service.name] = false
                }
            })
        }
    })

    return availability
}

export function getVideoLink(streamingOptions, serviceName, countryCode) {
    if (!streamingOptions?.streamingOptions) return null

    const option = streamingOptions.streamingOptions.find(opt => opt.country === countryCode)
    if (!option || !option.options) return null

    const service = option.options.find(s => s.service && s.service.name === serviceName)
    return service.videoLink ? service.videoLink : service.link || null
}